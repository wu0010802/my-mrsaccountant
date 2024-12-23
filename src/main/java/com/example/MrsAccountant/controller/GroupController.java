package com.example.mrsaccountant.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.mrsaccountant.entity.Group;
import com.example.mrsaccountant.entity.User;
import com.example.mrsaccountant.entity.UserGroup;
import com.example.mrsaccountant.service.GroupService;
import com.example.mrsaccountant.service.UserService;

@RestController
@RequestMapping("/mrsaccountant")
public class GroupController {

    private final UserService userService;
    private final GroupService groupService;

    public GroupController(UserService userService, GroupService groupService) {
        this.userService = userService;
        this.groupService = groupService;
    }

    @GetMapping("/user/groups")
    public ResponseEntity<?> getGroups(@AuthenticationPrincipal Long userId) {

        User user = userService.getUserById(userId);

        List<UserGroup> groups = user.getUserGroups();

        if (groups.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("No groups found for the user");
        }

        return ResponseEntity.ok(groups);
    }

    @GetMapping("/group/users/{groupId}")
    public ResponseEntity<?> getGroupUsers(@PathVariable("groupId") Long groupId) {
        Group retriveGroup = groupService.getGroupByGroupId(groupId);
        List<UserGroup> userInGroup = retriveGroup.getUserGroups();

        Set<Map<String, Object>> responseUsers = userInGroup.stream()
                .map(user -> {
                    Map<String, Object> userMap = new HashMap<>();
                    userMap.put("userId", user.getUser().getUserId());
                    userMap.put("username", user.getUser().getUsername());
                    userMap.put("userRole", user.getRole());
                    return userMap;
                })
                .collect(Collectors.toSet());

        return ResponseEntity.ok(responseUsers);
    }

    @PostMapping("/user/groups")
    public ResponseEntity<?> addGroupWithRole(
            @RequestBody Map<String, Object> requestBody,
            @AuthenticationPrincipal Long userId) {
        try {
            User user = userService.getUserById(userId);

            String groupName = (String) requestBody.get("groupName");
            String role = "ADMIN";

            if (groupName == null || groupName.isEmpty()) {
                return ResponseEntity.badRequest().body("Invalid group name");
            }

            UserGroup.GroupRole groupRole;
            try {
                groupRole = UserGroup.GroupRole.valueOf(role.toUpperCase());
            } catch (IllegalArgumentException e) {
                return ResponseEntity.badRequest().body("Invalid role: " + role);
            }

            Group group = new Group();
            group.setGroupName(groupName);
            groupService.createGroup(group);

            groupService.addGroup(group, user, groupRole);
            return ResponseEntity.status(HttpStatus.CREATED).body("Record added successfully with role: " + groupRole);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An unexpected error occurred");
        }
    }

    @PostMapping("/group/user/{userId}/{groupId}")
    public ResponseEntity<?> addUserToGroup(
            @AuthenticationPrincipal Long currentUserId,
            @PathVariable("userId") Long addedUserId,
            @PathVariable("groupId") Long groupId,
            @RequestParam(value = "role", required = false, defaultValue = "MEMBER") String role) {
        try {

            User currentUser = userService.getUserById(currentUserId);
            if (currentUser == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Current user not found with ID: " + currentUserId);
            }

            User addedUser = userService.getUserById(addedUserId);
            if (addedUser == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found with ID: " + addedUserId);
            }

            Group addedGroup = groupService.getGroupByGroupId(groupId);
            if (addedGroup == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Group not found with ID: " + groupId);
            }

            boolean isAdmin = groupService.isUserAdminOfGroup(currentUserId, groupId);
            if (!isAdmin) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Only admins can add users to the group.");
            }

            UserGroup.GroupRole groupRole;
            try {
                groupRole = UserGroup.GroupRole.valueOf(role.toUpperCase());
            } catch (IllegalArgumentException e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid role: " + role);
            }

            groupService.addGroup(addedGroup, addedUser, groupRole);

            return ResponseEntity.ok("User successfully added to the group with role: " + groupRole);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred.");
        }
    }

    // 剔除群組中的特定用戶，需加上用戶權限
    @DeleteMapping("/group/user/{userId}/{groupId}")
    public ResponseEntity<?> deleteGroupUser(
            @PathVariable("userId") Long deleteUserId,
            @PathVariable("groupId") Long groupId,
            @AuthenticationPrincipal Long currentUserId) {
        try {

            boolean isAdmin = groupService.isUserAdminOfGroup(currentUserId, groupId);
            if (!isAdmin) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Only admins can delete users from the group.");
            }

            if (currentUserId.equals(deleteUserId)) {
                boolean hasOtherAdmins = groupService.hasOtherAdmins(groupId, currentUserId);
                if (!hasOtherAdmins) {
                    return ResponseEntity.status(HttpStatus.FORBIDDEN)
                            .body("Admin must appoint at least one other admin before leaving the group.");
                }
            }

            groupService.removeGroupUser(deleteUserId, groupId);
            return ResponseEntity.ok("User removed from the group successfully.");

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred.");
        }
    }

    // 刪除整個群組
    @DeleteMapping("/user/groups/{groupId}")
    public ResponseEntity<?> deleteGroup(
            @PathVariable("groupId") Long groupId,
            @AuthenticationPrincipal Long userId) {
        try {

            User user = userService.getUserById(userId);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found with ID: " + userId);
            }

            boolean isAdmin = groupService.isUserAdminOfGroup(userId, groupId);
            if (!isAdmin) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Only admins can delete the group.");
            }

            groupService.deleteGroup(groupId);
            return ResponseEntity.status(HttpStatus.ACCEPTED)
                    .body("Group deleted successfully.");

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred.");
        }
    }

    @PutMapping("/group/user/{alteredUserId}/{groupId}")
    public ResponseEntity<?> alterGroupUserRole(@AuthenticationPrincipal Long currentUserId,
            @PathVariable Long alteredUserId, @PathVariable Long groupId, @RequestParam String role) {

        boolean isAdmin = groupService.isUserAdminOfGroup(currentUserId, groupId);
        if (!isAdmin) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Only admins can alter user roles in the group.");
        }

        UserGroup.GroupRole groupRole;
        try {

            groupRole = UserGroup.GroupRole.valueOf(role.toUpperCase());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid role: " + role);
        }

        try {

            groupService.alterGroupUserRole(alteredUserId, groupId, groupRole);
            return ResponseEntity.ok("User role updated successfully to: " + groupRole);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An unexpected error occurred while updating the user role.");
        }
    }

}

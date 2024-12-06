package com.example.mrsaccountant.controller;

import java.util.HashMap;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.mrsaccountant.entity.Group;
import com.example.mrsaccountant.entity.User;
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

    // 返回這個用戶有哪些群組
    @GetMapping("/user/groups")
    public ResponseEntity<?> getGroups(@AuthenticationPrincipal Long userId) {

        User user = userService.getUserById(userId);

        Set<Group> groups = user.getGroups();

        if (groups.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("No groups found for the user");
        }

        return ResponseEntity.ok(groups);
    }

    // 返回這個群組有哪些用戶
    @GetMapping("/group/users/{groupId}")
    public ResponseEntity<?> getGroupUsers(@PathVariable("groupId") Long groupId) {
        Group retriveGroup = groupService.getGroupByGroupId(groupId);
        Set<User> userInGroup = retriveGroup.getBelongUsers();

        Set<Map<String, Object>> responseUsers = userInGroup.stream()
                .map(user -> {
                    Map<String, Object> userMap = new HashMap<>();
                    userMap.put("userId", user.getUserId());
                    userMap.put("username", user.getUsername());
                    return userMap;
                })
                .collect(Collectors.toSet());

        return ResponseEntity.ok(responseUsers);
    }

    // 新增群組
    @PostMapping("/user/groups")
    public ResponseEntity<?> addGroup(
            @RequestBody Group group,
            @AuthenticationPrincipal Long userId) {
        try {

            User user = userService.getUserById(userId);

            if (group == null || group.getGroupName() == null || group.getGroupName().isEmpty()) {
                return ResponseEntity.badRequest().body("Invalid group data");
            }

            groupService.addGroup(group, user);
            return ResponseEntity.status(HttpStatus.CREATED).body("Record added successfully!");

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An unexpected error occurred");
        }
    }

    // 加入用戶至指定群組
    @PostMapping("/group/user/{userId}/{groupId}")
    public ResponseEntity<?> addUserToGroup(@PathVariable("userId") Long userId,
            @PathVariable("groupId") Long groupId) {
        try {
            User addedUser = userService.getUserById(userId);
            if (addedUser == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found with ID: " + userId);
            }

            Group addedGroup = groupService.getGroupByGroupId(groupId);
            if (addedGroup == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Group not found with ID: " + groupId);
            }

            groupService.addGroup(addedGroup, addedUser);
            return ResponseEntity.ok("User successfully added to the group.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred.");
        }
    }

    @DeleteMapping("/group/user/{userId}/{groupId}")
    public ResponseEntity<?> deleteGroupUser(
            @PathVariable("userId") Long userId,
            @PathVariable("groupId") Long groupId) {
        try {
            groupService.deleteGroupUser(groupId, userId);
            return ResponseEntity
                    .ok("User with ID: " + userId + " successfully removed from group with ID: " + groupId);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete user from group.");
        }
    }

    @DeleteMapping("/user/groups/{groupId}")
    public ResponseEntity<?> deleteGroup(@PathVariable("groupId") Long groupId,
            @AuthenticationPrincipal Long userId) {

        User user = userService.getUserById(userId);

        groupService.removeGroupFromUser(user.getUserId(), groupId);

        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body("Group deleted successfully!");
    }
}

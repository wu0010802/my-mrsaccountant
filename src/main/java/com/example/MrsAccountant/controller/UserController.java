package com.example.mrsaccountant.controller;

import java.util.Set;

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
// import com.example.mrsaccountant.security.CustomUserDetails;
import com.example.mrsaccountant.service.UserService;

@RestController
@RequestMapping("/mrsaccountant")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/user/groups")
    public ResponseEntity<?> getGroups(@AuthenticationPrincipal Long userId) {

        

        User user = userService.getUserById(userId);

        Set<Group> groups = user.getGroups();

        if (groups.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("No groups found for the user");
        }

        return ResponseEntity.ok(groups);
    }

    @PostMapping("/user/groups")
    public ResponseEntity<?> addGroup(
        @RequestBody Group group,
        @AuthenticationPrincipal Long userId
    ) {
        try {
           
            User user = userService.getUserById(userId);
    
            // 驗證群組資料是否有效
            if (group == null || group.getGroupName() == null || group.getGroupName().isEmpty()) {
                return ResponseEntity.badRequest().body("Invalid group data");
            }
    
            userService.addGroup(group, user);
            return ResponseEntity.status(HttpStatus.CREATED).body("Record added successfully!");
    
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("An unexpected error occurred");
        }
    }
    

    @DeleteMapping("/user/groups/{groupId}")
    public ResponseEntity<?> deleteGroup(@PathVariable("groupId") Long groupId,
            @AuthenticationPrincipal Long userId) {

      

        User user = userService.getUserById(userId);

        userService.removeGroupFromUser(user.getUserId(),groupId);



        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body("Group deleted successfully!");
    }
}

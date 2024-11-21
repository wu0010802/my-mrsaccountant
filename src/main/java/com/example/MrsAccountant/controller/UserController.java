package com.example.mrsaccountant.controller;

import java.util.Set;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.mrsaccountant.entity.Group;
import com.example.mrsaccountant.entity.User;
import com.example.mrsaccountant.security.CustomUserDetails;
import com.example.mrsaccountant.service.UserService;

@RestController
@RequestMapping("/mrsaccountant")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/user/groups")
    public ResponseEntity<?> getGroups(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        String userEmail = customUserDetails.getUsername();

        User user = userService.getUserByEmail(userEmail);

        Set<Group> groups = user.getGroup();
        
        return ResponseEntity.ok(groups);
    }
}

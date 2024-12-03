// package com.example.mrsaccountant.controller;

// import org.springframework.security.crypto.password.PasswordEncoder;
// import org.springframework.web.bind.annotation.*;

// import com.example.mrsaccountant.entity.User;
// import com.example.mrsaccountant.repository.UserRepository;

// @RestController
// @RequestMapping("/auth")
// public class AuthController {

//     private final PasswordEncoder passwordEncoder;
//     private final UserRepository userRepository;

//     public AuthController(PasswordEncoder passwordEncoder, UserRepository userRepository) {
//         this.passwordEncoder = passwordEncoder;
//         this.userRepository = userRepository;
//     }

//     // @PostMapping("/register")
//     // public String register(@RequestBody User user) {
        
//     //     user.setPassword(passwordEncoder.encode(user.getPassword()));

        
//     //     userRepository.save(user);

//     //     return "User registered successfully!";
//     // }
// }



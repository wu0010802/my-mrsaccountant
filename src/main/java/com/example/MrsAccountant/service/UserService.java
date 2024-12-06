package com.example.mrsaccountant.service;

import org.springframework.stereotype.Service;
import com.example.mrsaccountant.entity.User;
import com.example.mrsaccountant.repository.UserRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;

    }

    public User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("找不到UserId " + userId + " 的用戶"));

    }

    public void updateUser(User user) {
        userRepository.save(user);
    }

}

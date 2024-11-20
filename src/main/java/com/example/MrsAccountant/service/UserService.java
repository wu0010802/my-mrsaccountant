package com.example.mrsaccountant.service;

// import java.util.List;

import org.springframework.stereotype.Service;

// import com.example.mrsaccountant.entity.Record;
import com.example.mrsaccountant.entity.User;
// import com.example.mrsaccountant.repository.RecordRepository;
import com.example.mrsaccountant.repository.UserRepository;
// import java.util.Optional;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getUserByEmail(String email){
        return userRepository.findByEmail(email)
        .orElseThrow(() -> new UsernameNotFoundException("找不到電子郵件為 " + email + " 的用戶"));


    }

}


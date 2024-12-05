package com.example.mrsaccountant.service;

// import java.util.List;

import org.springframework.stereotype.Service;

import com.example.mrsaccountant.entity.User;
import com.example.mrsaccountant.entity.Group;
import com.example.mrsaccountant.repository.GroupRespository;
import com.example.mrsaccountant.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final GroupRespository groupRespository;

    public UserService(UserRepository userRepository, GroupRespository groupRespository) {
        this.userRepository = userRepository;
        this.groupRespository = groupRespository;
    }



    public User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("找不到UserId " + userId + " 的用戶"));

    }


    public void updateUser(User user){
        userRepository.save(user);
    }



    

}

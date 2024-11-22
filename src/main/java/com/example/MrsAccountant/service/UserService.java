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

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("找不到電子郵件為 " + email + " 的用戶"));

    }

    public void addGroup(Group group, User user) {
        
        if (user.getGroups().stream().anyMatch(g -> g.getGroupName().equals(group.getGroupName()))) {
            throw new IllegalArgumentException("The group is already associated with the user");
        }
    
       
        Group savedGroup = groupRespository.save(group);
        user.getGroups().add(savedGroup);
        userRepository.save(user);
        
    }

    @Transactional
    public void removeGroupFromUser(Long userId, Long groupId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        Group group = groupRespository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Group not found with id: " + groupId));

        if (user.getGroups().contains(group)) {
            user.getGroups().remove(group);
            userRepository.save(user);
        } else {
            throw new RuntimeException("Group not associated with the user");
        }
    }

}

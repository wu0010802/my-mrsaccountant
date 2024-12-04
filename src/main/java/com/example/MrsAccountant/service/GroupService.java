package com.example.mrsaccountant.service;


import org.springframework.stereotype.Service;

import com.example.mrsaccountant.entity.Group;
import com.example.mrsaccountant.entity.GroupTransaction;
import com.example.mrsaccountant.entity.User;
import com.example.mrsaccountant.repository.GroupRespository;
import com.example.mrsaccountant.repository.GroupTransactionRespository;
import com.example.mrsaccountant.repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class GroupService {
    private final GroupRespository groupRespository;

    private final GroupTransactionRespository groupTransactionRespository;
    private final UserRepository userRepository;

    public GroupService(GroupRespository groupRespository, GroupTransactionRespository groupTransactionRespository,
            UserRepository userRepository) {
        this.groupRespository = groupRespository;
        this.groupTransactionRespository = groupTransactionRespository;
        this.userRepository = userRepository;
    }


    public Group getGroupByGroupId(Long groupId){
        return groupRespository.findById(groupId).orElse(null);
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

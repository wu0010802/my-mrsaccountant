package com.example.mrsaccountant.service;



import org.springframework.stereotype.Service;
import com.example.mrsaccountant.entity.Group;
import com.example.mrsaccountant.entity.User;
import com.example.mrsaccountant.repository.GroupRespository;
import com.example.mrsaccountant.repository.UserRepository;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class GroupService {
    private final GroupRespository groupRespository;

    private final UserRepository userRepository;

    public GroupService(GroupRespository groupRespository,
            UserRepository userRepository) {
        this.groupRespository = groupRespository;
        this.userRepository = userRepository;
    }

    public Group getGroupByGroupId(Long groupId) {
        return groupRespository.findById(groupId).orElse(null);
    }

    public void addGroup(Group group, User user) {
        if (user.getGroups().stream().anyMatch(g -> g.getGroupName().equals(group.getGroupName()))) {
            throw new IllegalArgumentException("The group is already associated with the user");
        }

        user.getGroups().add(group);
        userRepository.save(user);
    }

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

    public void deleteGroupUser(Long groupId, Long userId) {

        Group group = groupRespository.findById(groupId)
                .orElseThrow(() -> new IllegalArgumentException("Group not found with ID: " + groupId));
        
   
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));
        
   
        if (!group.getBelongUsers().contains(user)) {
            throw new IllegalStateException("User with ID: " + userId + " is not in the group with ID: " + groupId);
        }
    
        
        group.getBelongUsers().remove(user);
        groupRespository.save(group);
    }
    

}

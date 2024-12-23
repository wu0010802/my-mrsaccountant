package com.example.mrsaccountant.service;

import org.springframework.stereotype.Service;
import com.example.mrsaccountant.entity.Group;
import com.example.mrsaccountant.entity.User;
import com.example.mrsaccountant.entity.UserGroup;
import com.example.mrsaccountant.entity.UserGroupId;
import com.example.mrsaccountant.repository.GroupRespository;
import com.example.mrsaccountant.repository.UserGroupRepository;
import com.example.mrsaccountant.repository.UserRepository;
import jakarta.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class GroupService {
    private final GroupRespository groupRespository;
    private final UserGroupRepository userGroupRepository;
    private final UserRepository userRepository;

    public GroupService(GroupRespository groupRespository,
            UserRepository userRepository, UserGroupRepository userGroupRepository) {
        this.groupRespository = groupRespository;
        this.userRepository = userRepository;
        this.userGroupRepository = userGroupRepository;
    }

    public Group getGroupByGroupId(Long groupId) {
        return groupRespository.findById(groupId).orElse(null);
    }

    public void createGroup(Group group){
        groupRespository.save(group);
    }

    public void addGroup(Group group, User user, UserGroup.GroupRole role) {
        UserGroup addedUserGroup = new UserGroup();
        addedUserGroup.setGroup(group);
        addedUserGroup.setUser(user);
        addedUserGroup.setRole(role);
        
        userGroupRepository.save(addedUserGroup);
    }

    public void removeGroupUser(Long userId, Long groupId) {

        UserGroupId removeUserGroupId = new UserGroupId();
        removeUserGroupId.setGroupId(groupId);
        removeUserGroupId.setUserId(userId);
        

        UserGroup removedUserGroup = new UserGroup();

        removedUserGroup.setId(removeUserGroupId);


        userGroupRepository.delete(removedUserGroup);
    }

    public boolean isUserAdminOfGroup(long currentUserId, long groupId) {
        UserGroupId userGroupId = new UserGroupId();
        userGroupId.setGroupId(groupId);
        userGroupId.setUserId(currentUserId);
        
        // 使用 Optional 的方法避免顯式捕捉異常
        return userGroupRepository.findById(userGroupId)
                .map(userGroup -> userGroup.getRole() == UserGroup.GroupRole.ADMIN)
                .orElse(false);
    }
    

    // public void deleteGroupUser(Long groupId, Long userId) {

    //     Group group = groupRespository.findById(groupId)
    //             .orElseThrow(() -> new IllegalArgumentException("Group not found with ID: " + groupId));

    //     User user = userRepository.findById(userId)
    //             .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));

    //     if (!group.getBelongUsers().contains(user)) {
    //         throw new IllegalStateException("User with ID: " + userId + " is not in the group with ID: " + groupId);
    //     }

    //     group.getBelongUsers().remove(user);
    //     groupRespository.save(group);
    // }

    public void deleteGroup(long groupId) {
        Group deletedGroup = groupRespository.findById(groupId).orElseThrow(
                () -> new IllegalArgumentException("Group not found with ID: " + groupId));
        groupRespository.delete(deletedGroup);
    }

    public boolean hasOtherAdmins(long groupId, long currentAdminId) {
        List<UserGroup> admins = userGroupRepository.findAllByGroupIdAndRole(groupId, UserGroup.GroupRole.ADMIN);
        return admins.stream().anyMatch(admin -> !admin.getId().getUserId().equals(currentAdminId));
    }


    public void alterGroupUserRole(Long alteredUserId, Long groupId, UserGroup.GroupRole groupRole) {

        UserGroupId userGroupId = new UserGroupId();
        userGroupId.setGroupId(groupId);
        userGroupId.setUserId(alteredUserId);
    
        UserGroup alterUser = userGroupRepository.findById(userGroupId)
                .orElseThrow(() -> new IllegalArgumentException("User or Group not found for given IDs."));
        

        alterUser.setRole(groupRole);
        userGroupRepository.save(alterUser);
    }

}

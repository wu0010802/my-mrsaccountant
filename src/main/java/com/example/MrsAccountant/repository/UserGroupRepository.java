package com.example.mrsaccountant.repository;


import com.example.mrsaccountant.entity.UserGroup;
import com.example.mrsaccountant.entity.UserGroupId;

import java.util.List;

import javax.management.relation.Role;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserGroupRepository extends JpaRepository<UserGroup, UserGroupId> {
    List<UserGroup> findByGroupId(Long GroupId);
    List<UserGroup> findAllByGroupIdAndRole(Long groupId, UserGroup.GroupRole role);
    
}

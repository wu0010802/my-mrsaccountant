package com.example.mrsaccountant.repository;


import com.example.mrsaccountant.entity.UserGroup;
import com.example.mrsaccountant.entity.UserGroupId;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserGroupRepository extends JpaRepository<UserGroup, UserGroupId> {
    List<UserGroup> findByGroupId(Long GroupId);
}

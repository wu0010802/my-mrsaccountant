package com.example.mrsaccountant.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.mrsaccountant.entity.Group;

@Repository
public interface GroupRespository extends JpaRepository<Group, Long>{



}

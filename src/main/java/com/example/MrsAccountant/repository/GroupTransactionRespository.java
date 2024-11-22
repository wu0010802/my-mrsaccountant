package com.example.mrsaccountant.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.mrsaccountant.entity.GroupTransaction;
import java.util.List;

@Repository
public interface GroupTransactionRespository extends JpaRepository<GroupTransaction, Long> {
    List<GroupTransaction> findGroupTransactionsByGroupId(Long groupId);
}

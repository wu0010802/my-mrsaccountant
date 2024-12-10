package com.example.mrsaccountant.repository;

import com.example.mrsaccountant.entity.Settlement;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SettlementRepository extends JpaRepository<Settlement, Long> {

    @Query("SELECT s FROM Settlement s WHERE s.group.id = :groupId AND s.id IN " +
           "(SELECT MAX(s2.id) FROM Settlement s2 WHERE s2.group.id = :groupId GROUP BY s2.user.id)")
    List<Settlement> findByGroupId(@Param("groupId") Long groupId);

}

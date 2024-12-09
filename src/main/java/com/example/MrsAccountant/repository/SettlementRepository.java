package com.example.mrsaccountant.repository;

import com.example.mrsaccountant.entity.Setttlement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SettlementRepository extends JpaRepository<Setttlement, Long> {
    
}

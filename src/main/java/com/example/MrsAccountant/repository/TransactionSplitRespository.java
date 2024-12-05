package com.example.mrsaccountant.repository;

import com.example.mrsaccountant.entity.Record;
import com.example.mrsaccountant.entity.TransactionSplit;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionSplitRespository extends JpaRepository<TransactionSplit, Long> {

}

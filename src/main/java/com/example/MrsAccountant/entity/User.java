package com.example.mrsaccountant.entity;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "users")
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(unique = true)
    private String lineId;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<UserGroup> userGroups = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TransactionSplit> transactionSplits = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Record> records;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Settlement> settlements = new ArrayList<>();

    public List<Settlement> getSettlements() {
        return settlements;
    }

    public Long getUserId() {
        return id;
    }

    public void setUserId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getLineId() {
        return lineId;
    }

    public void setLineId(String lineId) {
        this.lineId = lineId;
    }

    public List<Record> getRecords() {
        return records;
    }

    public void setRecords(List<Record> records) {
        this.records = records;
    }

    public void addRecord(Record record) {
        records.add(record);
        record.setUser(this);
    }

    public void removeRecord(Record record) {
        records.remove(record);
        record.setUser(null);
    }

    public void addTransactionSplit(TransactionSplit split) {
        transactionSplits.add(split);
        split.setUser(this);
    }

    public void removeTransactionSplit(TransactionSplit split) {
        transactionSplits.remove(split);
        split.setUser(null);
    }

    public void setSettlements(List<Settlement> settlements) {
        this.settlements = settlements;
    }

    public void addSettlement(Settlement settlement) {
        settlements.add(settlement);
        settlement.setUser(this);
    }

    public void removeSettlement(Settlement settlement) {
        settlements.remove(settlement);
        settlement.setUser(null);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true; 
        if (o == null || getClass() != o.getClass())
            return false; 
        User user = (User) o;
        return Objects.equals(id, user.id); 
    }

    public List<UserGroup> getUserGroups() {
        return userGroups;
    }

    public void setUserGroups(List<UserGroup> userGroups) {
        this.userGroups = userGroups;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "User{id=" + id + ", username='" + username + "'}";
    }

}

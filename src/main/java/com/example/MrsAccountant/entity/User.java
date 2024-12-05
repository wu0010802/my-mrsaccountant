package com.example.mrsaccountant.entity;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    @ManyToMany
    @JoinTable(name = "user_group", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "group_id"))
    private Set<Group> belongGroups = new HashSet<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TransactionSplit> transactionSplits = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Record> records;

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

    public Set<Group> getGroups() {
        return belongGroups;
    }

    public void setGroups(Set<Group> belongGroups) {
        this.belongGroups = belongGroups;
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
}

package com.example.mrsaccountant.entity;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;

@Entity
@Table(name = "split_group")
public class Group {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String groupName;


    @ManyToMany(mappedBy = "belongGroups")
    @JsonIgnore
    private Set<User> belongUsers = new HashSet<>(); 

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<GroupTransaction> groupTransactions;

    
    public Long getGroupId() {
        return id;
    }

    public void setGroupId(Long id) {
        this.id = id;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public Set<User> getBelongUsers() {
        return belongUsers;
    }

    public void setBelongUsers(Set<User> belongUsers) {
        this.belongUsers = belongUsers;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    
    @PrePersist
    public void setCreateAt() {
        this.createdAt = LocalDateTime.now();
    }

    public List<GroupTransaction> getGroupTransactions() {
        return groupTransactions;
    }

    public void setGroupTransactions(List<GroupTransaction> groupTransactions) {
        this.groupTransactions = groupTransactions;
    }

    public void addGroupTransaction(GroupTransaction groupTransaction) {
        groupTransactions.add(groupTransaction);
        groupTransaction.setGroup(this);
    }

    public void removeRecord(GroupTransaction groupTransaction) {
        groupTransactions.remove(groupTransaction);
        groupTransaction.setGroup(null);
    }

    public void addUser(User user) {
        if (this.belongUsers == null) {
            this.belongUsers = new HashSet<>();
        }
        this.belongUsers.add(user);
        user.getGroups().add(this);
    }
    
}

package com.example.mrsaccountant.entity;

import java.time.LocalDateTime;
import java.util.HashSet;
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
}

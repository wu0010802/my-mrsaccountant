package com.example.mrsaccountant.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.example.mrsaccountant.entity.UserGroup.GroupRole;
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

    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<UserGroup> userGroups = new ArrayList<>();

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<GroupTransaction> groupTransactions;

    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Settlement> settlements = new ArrayList<>();

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

    public List<UserGroup> getUserGroups() {
        return userGroups;
    }

    public void setUserGroups(List<UserGroup> userGroups) {
        this.userGroups = userGroups;
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

    public List<Settlement> getSettlements() {
        return settlements;
    }

    public void setSettlements(List<Settlement> settlements) {
        this.settlements = settlements;
    }

    public void addSettlement(Settlement settlement) {
        settlements.add(settlement);
        settlement.setGroup(this);
    }

    public void removeSettlement(Settlement settlement) {
        settlements.remove(settlement);
        settlement.setGroup(null);
    }

    public void addUserToGroup(User user, Group group, GroupRole role) {
        UserGroup userGroup = new UserGroup();
        userGroup.setUser(user);
        userGroup.setGroup(group);
        userGroup.setRole(role);

        user.getUserGroups().add(userGroup);
        group.getUserGroups().add(userGroup);
    }

    public GroupRole getUserRoleInGroup(User user, Group group) {
        return user.getUserGroups().stream()
                .filter(userGroup -> userGroup.getGroup().equals(group))
                .map(UserGroup::getRole)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("User is not in the group"));
    }

}

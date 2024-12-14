package com.example.mrsaccountant.entity;

import jakarta.persistence.*;


@Entity
@Table(name = "user_group")
public class UserGroup {

    @EmbeddedId
    private UserGroupId id = new UserGroupId(); 

    @ManyToOne
    @MapsId("userId") 
    private User user;

    @ManyToOne
    @MapsId("groupId") 
    private Group group;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private GroupRole role;

   
    public UserGroupId getId() {
        return id;
    }

    public void setId(UserGroupId id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public GroupRole getRole() {
        return role;
    }

    public void setRole(GroupRole role) {
        this.role = role;
    }

    public enum GroupRole {
        ADMIN, MEMBER, VISITOR
    }

}

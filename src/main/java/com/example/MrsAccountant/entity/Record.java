package com.example.mrsaccountant.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "records")
public class Record {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private User user;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Double amount;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Type type;

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false)
    private String category;

    // @Column(nullable = false)
    // private String note;

    // @Column(name = "created_at", updatable = false)
    // private LocalDateTime createdAt;

    // @PrePersist
    // protected void onCreate() {
    //     createdAt = LocalDateTime.now();
    // }


    public Long getRecordId() {
        return id;
    }

    public void setRecordId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    // public String getNote() {
    //     return note;
    // }

    // public void setNote(String note) {
    //     this.note = note;
    // }

    // public LocalDateTime getCreatedAt() {
    //     return createdAt;
    // }

    public enum Type {
        INCOME, EXPENSE
    }
}

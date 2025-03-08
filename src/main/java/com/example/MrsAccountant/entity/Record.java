package com.example.mrsaccountant.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table(name = "records",indexes = {@Index(name = "record_user_id_key", columnList = "id", unique = true)})
public class Record {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private User user;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "transaction_split_id", unique = true) 
    @JsonIgnore
    private TransactionSplit transactionSplit;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Double amount;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Type type;

    @Column(nullable = false)
    private String date;

    @Column(nullable = false)
    private String category;

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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public enum Type {
        INCOME, EXPENSE,TRANSFER
    }

    public TransactionSplit getTransactionSplit() {
        return transactionSplit;
    }

    public void setTransactionSplit(TransactionSplit transactionSplit) {
        this.transactionSplit = transactionSplit;
    }
}
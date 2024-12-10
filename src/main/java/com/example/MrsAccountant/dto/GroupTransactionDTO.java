package com.example.mrsaccountant.dto;

import java.util.List;

public class GroupTransactionDTO {
    private Double amount; 
    private String type; 
    private String category; 
    private String date;
    private String name; 
    private List<TransactionSplitDTO> transactionSplits; 

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<TransactionSplitDTO> getTransactionSplits() {
        return transactionSplits;
    }

    public void setTransactionSplits(List<TransactionSplitDTO> transactionSplits) {
        this.transactionSplits = transactionSplits;
    }


    
}

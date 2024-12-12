package com.example.mrsaccountant.dto;

public class SettlementStatusDTO {
    private String creditorName;
    private String debtorName;
    private Double replyAmount;


    public SettlementStatusDTO(String creditorName, String debtorName, Double replyAmount) {
        this.creditorName = creditorName;
        this.debtorName = debtorName;
        this.replyAmount = replyAmount;
    }

    public String getCreditorName() {
        return creditorName;
    }

    public void setCreditorName(String creditorName) {
        this.creditorName = creditorName;
    }

    public String getDebtorName() {
        return debtorName;
    }

    public void setDebtorName(String debtorName) {
        this.debtorName = debtorName;
    }

    public Double getReplyAmount() {
        return replyAmount;
    }

    public void setReplyAmount(Double replyAmount) {
        this.replyAmount = replyAmount;
    }
}
package edu.rmit.sef.account.model;

import java.util.Date;

public class AccountTransaction {

    private String AccountTransactionId;
    private double amount;
    private Date executedOn;
    private String description;
    private String ownerId;

    public String getAccountTransactionId() {
        return AccountTransactionId;
    }

    public void setAccountTransactionId(String accountTransactionId) {
        AccountTransactionId = accountTransactionId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Date getExecutedOn() {
        return executedOn;
    }

    public void setExecutedOn(Date executedOn) {
        this.executedOn = executedOn;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

}

package edu.rmit.sef.order.model;

import java.util.Date;
import java.util.UUID;

public class OrderLineTransaction {

    private String orderLineTransactionId;
    private long quantity;
    private double executedPrice;
    private Date executedOn;

    public OrderLineTransaction(long quantity, double executedPrice, Date executedOn) {
        this.quantity = quantity;
        this.orderLineTransactionId = UUID.randomUUID().toString();
        this.executedPrice = executedPrice;
        this.executedOn = executedOn;
    }


    public long getQuantity() {
        return quantity;
    }

    public void setQuantity(long quantity) {
        this.quantity = quantity;
    }

    public String getOrderLineTransactionId() {
        return orderLineTransactionId;
    }

    public void setOrderLineTransactionId(String orderLineTransactionId) {
        this.orderLineTransactionId = orderLineTransactionId;
    }

    public Date getExecutedOn() {
        return executedOn;
    }

    public void setExecutedOn(Date executedOn) {
        this.executedOn = executedOn;
    }

    public double getExecutedPrice() {
        return executedPrice;
    }

    public void setExecutedPrice(double executedPrice) {
        this.executedPrice = executedPrice;
    }


}

package edu.rmit.sef.order.model;

import java.util.Date;

public class OrderLineTransaction {

    private String orderId;
    private long quantity;
    private String orderLineTransactionId;
    private double executedPrice;
    private Date executedOn;

    public OrderLineTransaction(String orderId, long quantity, String orderLineTransactionId, double executedPrice, Date executedOn) {
        this.orderId = orderId;
        this.quantity = quantity;
        this.orderLineTransactionId = orderLineTransactionId;
        this.executedPrice = executedPrice;
        this.executedOn = executedOn;
    }

    public OrderLineTransaction() {
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
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

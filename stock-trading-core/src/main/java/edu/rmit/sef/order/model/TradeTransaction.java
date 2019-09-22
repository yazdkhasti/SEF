package edu.rmit.sef.order.model;

import java.util.Date;

public class TradeTransaction {

    private String masterTransactionId;
    private String buyerOrderId;
    private String sellerOrderId;
    private String buyerOrderLineTransactionId;
    private String sellerOrderLineTransactionId;
    private long tradeQuantity;
    private double executedPrice;
    private Date executedOn;

    public String getMasterTransactionId() {
        return masterTransactionId;
    }

    public void setMasterTransactionId(String masterTransactionId) {
        this.masterTransactionId = masterTransactionId;
    }

    public String getBuyerOrderId() {
        return buyerOrderId;
    }

    public void setBuyerOrderId(String buyerOrderId) {
        this.buyerOrderId = buyerOrderId;
    }

    public String getSellerOrderId() {
        return sellerOrderId;
    }

    public void setSellerOrderId(String sellerOrderId) {
        this.sellerOrderId = sellerOrderId;
    }

    public String getBuyerOrderLineTransactionId() {
        return buyerOrderLineTransactionId;
    }

    public void setBuyerOrderLineTransactionId(String buyerOrderLineTransactionId) {
        this.buyerOrderLineTransactionId = buyerOrderLineTransactionId;
    }

    public String getSellerOrderLineTransactionId() {
        return sellerOrderLineTransactionId;
    }

    public void setSellerOrderLineTransactionId(String sellerOrderLineTransactionId) {
        this.sellerOrderLineTransactionId = sellerOrderLineTransactionId;
    }

    public long getTradeQuantity() {
        return tradeQuantity;
    }

    public void setTradeQuantity(long tradeQuantity) {
        this.tradeQuantity = tradeQuantity;
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

package edu.rmit.sef.order.model;

import java.util.Date;

public class OrderMatchedEvent {

    private String orderNumber;
    private String stockSymbol;
    private int tradeQuantity;
    private Date executedOn;

    public OrderMatchedEvent(String orderNumber, String stockSymbol, int tradeQuantity, Date executedOn) {
        this.orderNumber = orderNumber;
        this.stockSymbol = stockSymbol;
        this.tradeQuantity = tradeQuantity;
        this.executedOn = executedOn;
    }

    public OrderMatchedEvent() {
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getStockSymbol() {
        return stockSymbol;
    }

    public void setStockSymbol(String stockSymbol) {
        this.stockSymbol = stockSymbol;
    }

    public int getTradeQuantity() {
        return tradeQuantity;
    }

    public void setTradeQuantity(int tradeQuantity) {
        this.tradeQuantity = tradeQuantity;
    }

    public Date getExecutedOn() {
        return executedOn;
    }

    public void setExecutedOn(Date executedOn) {
        this.executedOn = executedOn;
    }

}

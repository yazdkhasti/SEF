package edu.rmit.sef.order.command;

import edu.rmit.command.core.Command;
import edu.rmit.sef.core.command.CreateEntityResp;
import edu.rmit.sef.order.model.OrderType;

public class CreateOrderCmd extends Command<CreateEntityResp> {
    private String orderNumber;
    private double price;
    private int quantity;
    private String userId;
    private String stockId;
    private OrderType orderType;

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getStockId() {
        return stockId;
    }

    public void setStockId(String stockId) {
        this.stockId = stockId;
    }

    public OrderType getOrderType() {
        return orderType;
    }

    public void setOrderType(OrderType orderType) {
        this.orderType = orderType;
    }
}

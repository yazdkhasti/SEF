package edu.rmit.sef.order.model;

import edu.rmit.command.core.CommandUtil;
import edu.rmit.sef.core.model.Entity;

public class Order extends Entity {

    private String transactionId;
    private double price;
    private int quantity;
    private OrderType orderType;
    private OrderState orderState;
    private String stockId;

    private int remainedQuantity;

    public String getStockId() {
        return stockId;
    }

    public void setStockId(String stockId) {
        this.stockId = stockId;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId =
                transactionId;
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

    public OrderType getOrderType() {
        return orderType;
    }

    public void setOrderType(OrderType orderType) {
        this.orderType = orderType;
    }

    public OrderState getOrderState() {
        return orderState;
    }

    public void setOrderState(OrderState orderState) {
        this.orderState = orderState;
    }

    public int getRemainedQuantity() {
        return remainedQuantity;
    }

    public void setRemainedQuantity(int remainedQuantity) {
        this.remainedQuantity = remainedQuantity;
    }

    public void trade(int quantity) {
        if (this.remainedQuantity < quantity) {
            CommandUtil.throwAppExecutionException("Trade quantitu cannot exceed remaining quantity.");
        }
        if (this.remainedQuantity > quantity) {
            orderState = OrderState.PartiallyTraded;
        } else {
            orderState = OrderState.TradedCompletely;
        }
        remainedQuantity -= quantity;
    }

    public void withdraw() {
        if (orderState == OrderState.TradedCompletely || orderState == OrderState.PartiallyCanceled || orderState == OrderState.Canceled) {
            CommandUtil.throwAppExecutionException("The order state is not valid for withdraw");
        }

        remainedQuantity = 0;

        if (orderState == OrderState.PartiallyTraded) {
            orderState = OrderState.PartiallyCanceled;
        } else {
            orderState = OrderState.Canceled;
        }
    }

    public static String getTransactionId(Long orderNumber) {
       return  String.format("TR%1$12s", orderNumber).replace(' ', '0');
    }


}

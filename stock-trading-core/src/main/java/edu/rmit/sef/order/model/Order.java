package edu.rmit.sef.order.model;

import edu.rmit.command.core.CommandUtil;
import edu.rmit.sef.core.model.Entity;

public class Order extends Entity {

    private String transactionId;
    private double price;
    private long quantity;
    private OrderType orderType;
    private OrderState orderState;
    private String stockId;
    private long remainedQuantity;

    public Order() {
        this.orderState = OrderState.PendingTrade;
    }

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

    public long getQuantity() {
        return quantity;
    }

    public void setQuantity(long quantity) {
        this.quantity = quantity;
        this.remainedQuantity = quantity;
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

    public long getRemainedQuantity() {
        return remainedQuantity;
    }

    public boolean validForTrade() {
        boolean result = false;
        if ((this.orderState == OrderState.PendingTrade || this.orderState == OrderState.PartiallyTraded) && remainedQuantity > 0) {
            result = true;
        }
        return result;
    }

    public void trade(long quantity) {

        CommandUtil.must(() -> validForTrade(), "Order is not in valid state for trade");

        if (this.remainedQuantity < quantity) {
            CommandUtil.throwAppExecutionException("Trade quantity cannot exceed remaining quantity.");
        }
        if (this.remainedQuantity > quantity) {
            orderState = OrderState.PartiallyTraded;
        } else {
            orderState = OrderState.TradedCompletely;
        }
        remainedQuantity -= quantity;
    }

    public boolean validForWithdraw() {
        boolean result = false;
        if (orderState == OrderState.PendingTrade || orderState == OrderState.PartiallyTraded) {
            result = true;
        }
        return result;
    }

    public void withdraw() {

        CommandUtil.must(() -> validForWithdraw(), "The order state is not valid for withdraw");

        remainedQuantity = 0;

        if (orderState == OrderState.PartiallyTraded) {
            orderState = OrderState.PartiallyCanceled;
        } else {
            orderState = OrderState.Canceled;
        }
    }

    public static String getTransactionId(Long orderNumber) {
        return String.format("TR%1$12s", orderNumber).replace(' ', '0');
    }


}

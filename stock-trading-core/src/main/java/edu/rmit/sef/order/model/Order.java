package edu.rmit.sef.order.model;

import edu.rmit.command.core.CommandUtil;
import edu.rmit.sef.core.model.Entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Order extends Entity {

    private String transactionId;
    private double price;
    private long quantity;
    private OrderType orderType;
    private OrderState orderState;
    private String stockId;
    private List<OrderLineTransaction> lines;
    private long remainedQuantity;

    public Order() {
        this.orderState = OrderState.PendingTrade;
        this.lines = new ArrayList<>();
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

    public OrderLineTransaction trade(long quantity, double price, Date executedOn) {

        CommandUtil.must(() -> validForTrade(), "Order is not in valid state for trade");

        CommandUtil.must(() -> this.remainedQuantity >= quantity, "Trade quantity cannot exceed remaining quantity.");

        CommandUtil.must(() -> this.price >= price, "Price cannot be greater than order price");

        if (this.remainedQuantity > quantity) {
            orderState = OrderState.PartiallyTraded;
        } else {
            orderState = OrderState.TradedCompletely;
        }
        remainedQuantity -= quantity;

        OrderLineTransaction line = new OrderLineTransaction(quantity, price, executedOn);
        this.lines.add(line);

        return line;
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

        if (orderState == OrderState.PartiallyTraded) {
            orderState = OrderState.PartiallyCanceled;
        } else {
            orderState = OrderState.Canceled;
        }
    }

    public static String formatTransactionId(long orderNumber) {
        return String.format("TR%1$12s", orderNumber).replace(' ', '0');
    }

    public OrderLineTransaction[] getLines() {
        return lines.toArray(new OrderLineTransaction[0]);
    }


}

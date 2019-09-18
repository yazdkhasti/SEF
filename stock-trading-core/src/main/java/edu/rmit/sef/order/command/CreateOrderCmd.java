package edu.rmit.sef.order.command;

import edu.rmit.command.core.Command;
import edu.rmit.command.core.QueuedCommand;
import edu.rmit.sef.core.command.CreateEntityResp;
import edu.rmit.sef.order.model.OrderType;

@QueuedCommand
public class CreateOrderCmd extends Command<CreateEntityResp> {

    private double price;
    private int quantity;
    private OrderType orderType;
    private String stockId;

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

    public String getStockId() {
        return stockId;
    }

    public void setStockId(String stockId) {
        this.stockId = stockId;
    }


}

package edu.rmit.sef.order.command;

import edu.rmit.command.core.Command;
import edu.rmit.sef.core.command.CreateEntityResp;
import edu.rmit.sef.order.model.OrderState;
import edu.rmit.sef.order.model.OrderType;

public class CreateOrderCmd extends Command<CreateEntityResp> {

    private long price;
    private int quantity;
    private OrderType orderType;
    private String stockSymbol;

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
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

    public String getStockSymbol() {
        return stockSymbol;
    }

    public void setStockSymbol(String stockSymbol) {
        this.stockSymbol = stockSymbol;
    }


}

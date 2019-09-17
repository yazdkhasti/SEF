package edu.rmit.sef.stock.model;

import edu.rmit.command.core.CommandUtil;
import edu.rmit.sef.core.model.Entity;

public class Stock extends Entity {


    private String symbol;
    private String name;
    private double price;
    private StockState stockState;

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public StockState getStockState() {
        return stockState;
    }

    public void setStockState(StockState stockState) {
        this.stockState = stockState;
    }

    public void approve() {
        CommandUtil.must(() -> this.stockState == StockState.PendingApprove, "The stock is not in a valid state.");
        this.stockState = StockState.OnTrade;
    }

    public void disable() {
        CommandUtil.must(() -> this.stockState == StockState.OnTrade
                ||  this.stockState == StockState.PendingApprove, "The stock is not in a valid state.");

        this.stockState = StockState.Disabled;
    }

    @Override
    public void validate() {
        CommandUtil.assertNotNullArgument(this.getName());
        CommandUtil.assertNotNullArgument(this.getSymbol());
        CommandUtil.must(() -> this.getPrice() > 0, "The stock price cannot be less than or equal to zero. ");
    }


}

package edu.rmit.sef.stocktradingserver.order.command;

import edu.rmit.command.core.Command;
import edu.rmit.command.core.NullResp;
import edu.rmit.command.security.CommandAuthority;
import edu.rmit.sef.order.model.Order;
import edu.rmit.sef.order.model.OrderLineTransaction;

import java.util.Date;

@CommandAuthority()
public class OrderMatchedCmd extends Command<NullResp> {
    private Order buyOrder;
    private Order sellOrder;
    private OrderLineTransaction buyLine;
    private OrderLineTransaction sellLine;
    private long tradeQuantity;
    private double executedPrice;
    private Date executedOn;
    private String stockId;

    public OrderMatchedCmd(Order buyOrder, OrderLineTransaction buyLine, Order sellOrder, OrderLineTransaction sellLine, String stockId, long tradeQuantity, double executedPrice) {
        this.buyOrder = buyOrder;
        this.sellOrder = sellOrder;
        this.buyLine = buyLine;
        this.sellLine = sellLine;
        this.stockId = stockId;
        this.tradeQuantity = tradeQuantity;
        this.executedPrice = executedPrice;
        this.executedOn = new Date();
    }

    public OrderMatchedCmd() {
    }

    public Order getBuyOrder() {
        return buyOrder;
    }

    public void setBuyOrder(Order buyOrder) {
        this.buyOrder = buyOrder;
    }


    public Order getSellOrder() {
        return sellOrder;
    }

    public void setSellOrder(Order sellOrder) {
        this.sellOrder = sellOrder;
    }

    public Date getExecutedOn() {
        return executedOn;
    }

    public void setExecutedOn(Date executedOn) {
        this.executedOn = executedOn;
    }

    public long getTradeQuantity() {
        return tradeQuantity;
    }

    public void setTradeQuantity(long tradeQuantity) {
        this.tradeQuantity = tradeQuantity;
    }

    public double getExecutedPrice() {
        return executedPrice;
    }

    public void setExecutedPrice(double executedPrice) {
        this.executedPrice = executedPrice;
    }

    public String getStockId() {
        return stockId;
    }

    public OrderLineTransaction getBuyLine() {
        return buyLine;
    }

    public void setBuyLine(OrderLineTransaction buyLine) {
        this.buyLine = buyLine;
    }

    public OrderLineTransaction getSellLine() {
        return sellLine;
    }

    public void setSellLine(OrderLineTransaction sellLine) {
        this.sellLine = sellLine;
    }

    public void setStockId(String stockId) {
        this.stockId = stockId;
    }

}

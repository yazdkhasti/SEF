package edu.rmit.sef.order.command;

import edu.rmit.sef.order.model.OrderLineTransaction;

import java.util.List;

public class GetOrderTransactionLinesResp {

    private List<OrderLineTransaction> orderLineTransactions;

    public List<OrderLineTransaction> getOrderLineTransactions() {
        return orderLineTransactions;
    }

    public void setOrderLineTransactions(List<OrderLineTransaction> orderLineTransactions) {
        this.orderLineTransactions = orderLineTransactions;
    }

}

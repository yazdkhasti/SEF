package edu.rmit.sef.order.command;

import edu.rmit.sef.order.model.Order;

public class FindOrderByIdResp {

    private Order order;

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

}

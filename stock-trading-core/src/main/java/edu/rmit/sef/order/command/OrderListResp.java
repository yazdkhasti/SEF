package edu.rmit.sef.order.command;

import edu.rmit.sef.order.model.Order;

import java.util.List;

public class OrderListResp  {

    private List<Order> orderList;

    public OrderListResp(List<Order> orderList) {
        this.orderList = orderList;
    }

    public List<Order> getOrderList() {
        return orderList;
    }

    public void setOrderList(List<Order> orderList) {
        this.orderList = orderList;
    }

}

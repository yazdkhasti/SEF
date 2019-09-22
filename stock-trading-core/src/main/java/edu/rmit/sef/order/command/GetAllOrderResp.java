package edu.rmit.sef.order.command;

import edu.rmit.sef.core.command.GetAllResp;
import edu.rmit.sef.order.model.Order;
import edu.rmit.sef.stock.model.Stock;

import java.util.List;

public class GetAllOrderResp extends GetAllResp<Order> {
    private List<Order> orderList;

    public List<Order> getOrderList() {
        return orderList;
    }

    public void setOrderList(List<Order> orderList) {
        this.orderList = orderList;
    }

}

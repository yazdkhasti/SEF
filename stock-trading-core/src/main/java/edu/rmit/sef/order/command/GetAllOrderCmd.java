package edu.rmit.sef.order.command;

import edu.rmit.command.core.Command;
import edu.rmit.sef.core.command.CreateEntityResp;
import edu.rmit.sef.order.model.Order;

import java.util.List;

public class GetAllOrderCmd extends Command<CreateEntityResp> {
    private List<Order> orderList;

    public List<Order> getOrderList() {
        return orderList;
    }

    public void setOrderList(List<Order> orderList) {
        this.orderList = orderList;
    }
}

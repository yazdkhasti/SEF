package edu.rmit.sef.order.command;

import edu.rmit.command.core.Command;


public class FindOrderByIdCmd extends Command<FindOrderByIdResp> {

    private String orderId;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

}

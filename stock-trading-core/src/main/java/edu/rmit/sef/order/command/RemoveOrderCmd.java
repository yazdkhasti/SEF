package edu.rmit.sef.order.command;

import edu.rmit.command.core.Command;
import edu.rmit.sef.core.command.CreateEntityResp;

public class RemoveOrderCmd extends Command<CreateEntityResp> {
    private String orderNumber;

    public String getOrderNumber()  {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }
}

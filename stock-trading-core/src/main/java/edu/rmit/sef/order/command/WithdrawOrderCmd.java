package edu.rmit.sef.order.command;

import edu.rmit.command.core.Command;
import edu.rmit.command.core.EnableCustomKeySelector;
import edu.rmit.command.core.NullResp;
import edu.rmit.command.core.QueuedCommand;
import edu.rmit.sef.order.model.OrderState;
import edu.rmit.sef.order.model.OrderType;


@QueuedCommand
@EnableCustomKeySelector
public class WithdrawOrderCmd extends Command<NullResp> {


    private String orderId;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }


}

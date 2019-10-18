package edu.rmit.sef.order.command;

import edu.rmit.command.core.Command;
import edu.rmit.command.core.EnableCustomKeySelector;
import edu.rmit.command.core.NullResp;
import edu.rmit.command.core.QueuedCommand;
import edu.rmit.command.security.CommandAuthority;
import edu.rmit.sef.core.security.Authority;


@QueuedCommand
@EnableCustomKeySelector
@CommandAuthority(Authority.USER)
public class WithdrawOrderCmd extends Command<NullResp> {


    private String orderId;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }


}

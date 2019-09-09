package edu.rmit.sef.stocktradingserver.order.command;

import edu.rmit.command.core.Command;
import edu.rmit.command.core.EnableCustomKeySelector;
import edu.rmit.command.core.NullResp;
import edu.rmit.command.core.QueuedCommand;
import edu.rmit.sef.order.model.Order;

@QueuedCommand
@EnableCustomKeySelector
public class MatchOrderCmd extends Command<NullResp> {

    private Order order;


    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }
}

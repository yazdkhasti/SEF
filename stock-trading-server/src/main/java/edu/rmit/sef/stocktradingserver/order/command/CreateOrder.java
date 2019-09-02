package edu.rmit.sef.stocktradingserver.order.command;

import edu.rmit.command.core.Command;
import edu.rmit.sef.stocktradingserver.order.model.OrderType;
import edu.rmit.sef.stocktradingserver.test.core.command.CreateEntityResp;

public class CreateOrder extends Command<CreateEntityResp> {
    public String userId;
    public String quantity;
    public String stockId;
    public OrderType orderType;
}

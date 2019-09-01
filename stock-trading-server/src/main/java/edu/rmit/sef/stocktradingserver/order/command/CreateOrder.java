package edu.rmit.sef.stocktradingserver.order.command;

import edu.rmit.command.core.Command;
import edu.rmit.sef.stocktradingserver.core.command.CreateEntityResp;
import edu.rmit.sef.stocktradingserver.order.model.OrderType;

public class CreateOrder extends Command<CreateEntityResp> {
    public String userId;
    public String quantity;
    public String stockId;
    public OrderType orderType;
}

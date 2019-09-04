package edu.rmit.sef.stocktradingserver.order.service;

import edu.rmit.command.core.ICommandExecutionContext;
import edu.rmit.command.core.ICommandHandler;
import edu.rmit.sef.stocktradingserver.order.command.CreateOrder;
import edu.rmit.sef.core.command.CreateEntityResp;
import org.springframework.stereotype.Service;

@Service
public class OrderHandler implements ICommandHandler<CreateOrder> {
    @Override
    public void handle(ICommandExecutionContext<CreateOrder> executionContext)  {
        int i = 0;
        System.out.println("1");
        executionContext.getCommand().setResponse(new CreateEntityResp("0"));
    }
}

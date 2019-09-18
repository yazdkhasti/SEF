package edu.rmit.sef.stocktradingclient.order.service;

import edu.rmit.command.core.CommandUtil;
import edu.rmit.command.core.ICommandHandler;
import edu.rmit.sef.core.command.CreateEntityResp;
import edu.rmit.sef.core.model.Entity;
import edu.rmit.sef.core.model.SocketMessage;
import edu.rmit.sef.order.command.CreateOrderCmd;
import edu.rmit.sef.order.command.GetAllOrderCmd;
import edu.rmit.sef.order.command.RemoveOrderCmd;
import edu.rmit.sef.stocktradingclient.core.socket.SocketConnection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OrderService {

    @Autowired
    private SocketConnection socketConnection;

    @Bean
    public ICommandHandler<CreateOrderCmd> createOrderCmdICommandHandler() {

        return executionContext -> {

            CreateOrderCmd cmd = executionContext.getCommand();
            socketConnection.executeCommand(cmd);

        };
    }


    @Bean
    public ICommandHandler<GetAllOrderCmd> getAllOrderCmdICommandHandler() {

        return executionContext -> {

            GetAllOrderCmd cmd = executionContext.getCommand();
            socketConnection.executeCommand(cmd);

        };
    }
}


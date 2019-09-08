package edu.rmit.sef.stocktradingclient.stock.service;

import edu.rmit.command.core.CommandUtil;
import edu.rmit.command.core.ICommandHandler;
import edu.rmit.sef.core.command.CreateEntityResp;
import edu.rmit.sef.core.model.Entity;
import edu.rmit.sef.core.model.SocketMessage;
import edu.rmit.sef.stock.command.AddStockCmd;
import edu.rmit.sef.stock.model.Stock;
import edu.rmit.sef.stocktradingclient.core.socket.SocketConnection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StockService {

    @Autowired
    private SocketConnection socketConnection;

    @Bean
    public ICommandHandler<AddStockCmd> addStockHandler() {

        return executionContext -> {

            AddStockCmd cmd = executionContext.getCommand();
            socketConnection.executeCommand(cmd);

        };

    }
}


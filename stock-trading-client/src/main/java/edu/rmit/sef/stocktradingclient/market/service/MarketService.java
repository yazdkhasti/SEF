package edu.rmit.sef.stocktradingclient.market.service;

import edu.rmit.command.core.CommandUtil;
import edu.rmit.command.core.ICommandHandler;
import edu.rmit.sef.core.command.CreateEntityResp;
import edu.rmit.sef.core.model.Entity;
import edu.rmit.sef.core.model.SocketMessage;
import edu.rmit.sef.market.command.SetMarketCmd;
import edu.rmit.sef.market.model.Market;
import edu.rmit.sef.stocktradingclient.core.socket.SocketConnection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MarketService {

    @Autowired
    private SocketConnection socketConnection;

    @Bean
    public ICommandHandler<SetMarketCmd> setMarketCmdICommandHandler() {

        return executionContext -> {

            SetMarketCmd cmd = executionContext.getCommand();
            socketConnection.executeCommand(cmd);

        };

    }
}


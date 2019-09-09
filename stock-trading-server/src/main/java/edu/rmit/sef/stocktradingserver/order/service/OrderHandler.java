package edu.rmit.sef.stocktradingserver.order.service;


import edu.rmit.command.core.ICommandHandler;
import edu.rmit.command.core.IQueueKeySelector;
import edu.rmit.sef.order.command.PlaceOrderCmd;
import edu.rmit.sef.stocktradingserver.order.command.MatchOrderCmd;
import edu.rmit.sef.user.command.AuthenticateCmd;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


import java.util.Date;

@Configuration
public class OrderHandler {


    @Bean
    public IQueueKeySelector<MatchOrderCmd> matchOrderKeySelect() {

        return (command, tClass) -> command.getOrder().getStockSymbol();

    }

    @Bean
    public ICommandHandler<PlaceOrderCmd> placeOrderHandler() {

        return executionContext -> {


        };

    }

    @Bean
    public ICommandHandler<MatchOrderCmd> matchOrderHandler() {

        return executionContext -> {


        };

    }

}

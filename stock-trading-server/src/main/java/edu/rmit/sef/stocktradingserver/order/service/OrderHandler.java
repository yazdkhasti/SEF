package edu.rmit.sef.stocktradingserver.order.service;


import edu.rmit.command.core.ICommandHandler;
import edu.rmit.command.core.ICommandService;
import edu.rmit.command.core.IQueueKeySelector;
import edu.rmit.command.core.NullResp;
import edu.rmit.sef.order.command.PlaceOrderCmd;
import edu.rmit.sef.order.model.Order;
import edu.rmit.sef.order.model.OrderState;
import edu.rmit.sef.order.model.OrderType;
import edu.rmit.sef.stock.command.UpdateStockCmd;
import edu.rmit.sef.stocktradingserver.order.command.MatchOrderCmd;
import edu.rmit.sef.stocktradingserver.order.command.OrderMatchedCmd;
import edu.rmit.sef.stocktradingserver.order.repo.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

@Configuration
public class OrderHandler {

    @Autowired
    OrderRepository orderRepository;



}

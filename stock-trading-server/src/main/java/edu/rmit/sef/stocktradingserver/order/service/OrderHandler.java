package edu.rmit.sef.stocktradingserver.order.service;

import edu.rmit.command.core.ICommandHandler;
import edu.rmit.command.core.InitCmd;
import edu.rmit.sef.core.model.Entity;
import edu.rmit.sef.order.command.CreateOrderCmd;
import edu.rmit.sef.core.command.CreateEntityResp;
import edu.rmit.sef.order.command.GetAllOrdersCmd;
import edu.rmit.sef.order.model.Order;
import edu.rmit.sef.stocktradingserver.order.repo.OrderRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;


import java.util.List;
import java.util.concurrent.atomic.AtomicLong;


@Configuration
public class OrderHandler {


    AtomicLong lastOrderNumber = new AtomicLong();

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Value("${edu.rmit.sef.stocktrading.server.order.firstTransactionNumber}")
    long firstTransactionNumber;


    @Bean
    public ICommandHandler<InitCmd> initCmdICommandHandler() {

        return executionContext -> {

            long orderCount = orderRepository.count();
            lastOrderNumber.set(firstTransactionNumber + orderCount);

        };
    }


    @Bean
    public ICommandHandler<CreateOrderCmd> createOrderCmdICommandHandler() {

        return executionContext -> {

            CreateOrderCmd cmd = executionContext.getCommand();

            Order order = Entity.newEntity(executionContext.getUserId(), Order.class);

            modelMapper.getConfiguration().setAmbiguityIgnored(true);
            modelMapper.map(cmd, order);


            Long orderNumber = lastOrderNumber.getAndDecrement();
            String transactionId = Order.getTransactionId(orderNumber);
            order.setTransactionId(transactionId);


            orderRepository.insert(order);

            cmd.setResponse(new CreateEntityResp(order.getId()));


        };

    }


    @Bean
    public ICommandHandler<GetAllOrdersCmd> getAllOrderCmdICommandHandler() {

        return executionContext -> {



        };

    }


}

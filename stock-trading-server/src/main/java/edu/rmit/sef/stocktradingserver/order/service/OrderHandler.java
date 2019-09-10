package edu.rmit.sef.stocktradingserver.order.service;

import edu.rmit.command.core.CommandUtil;
import edu.rmit.command.core.ICommandExecutionContext;
import edu.rmit.command.core.ICommandHandler;
import edu.rmit.sef.core.model.Entity;
import edu.rmit.sef.order.command.CreateOrderCmd;
import edu.rmit.sef.core.command.CreateEntityResp;
import edu.rmit.sef.order.command.GetAllOrderCmd;
import edu.rmit.sef.order.command.RemoveOrderCmd;
import edu.rmit.sef.order.model.Order;
import edu.rmit.sef.stocktradingserver.order.repo.OrderRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;


@Configuration
public class OrderHandler{
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Bean
    public ICommandHandler<CreateOrderCmd> createOrderCmdICommandHandler() {

        return executionContext -> {

            CreateOrderCmd cmd = executionContext.getCommand();

            Order order = Entity.newEntity(cmd.getOrderNumber(), Order.class);

            modelMapper.getConfiguration().setAmbiguityIgnored(true);
            if (order == null) {
                CommandUtil.throwCommandExecutionException("Order can not be null");
            }

            modelMapper.map(cmd, order);



            Order duplicateOrder = orderRepository.findOrderByOrderNumber(order.getOrderNumber());

            if (duplicateOrder != null) {
                CommandUtil.throwCommandExecutionException("Order with same order number already exists.");
            }

            orderRepository.insert(order);

            cmd.setResponse(new CreateEntityResp(order.getId()));


        };

    }

    @Bean
    public ICommandHandler<RemoveOrderCmd> removeOrderCmdICommandHandler() {

        return executionContext -> {

            RemoveOrderCmd cmd = executionContext.getCommand();

            Order order;
            order = orderRepository.findOrderByOrderNumber(cmd.getOrderNumber());

            modelMapper.map(cmd, order);

            if (order.getOrderNumber() == null) {
                CommandUtil.throwCommandExecutionException("Order not exist.");
            }

            cmd.setResponse(new CreateEntityResp(order.getId()));
            orderRepository.delete(order);

        };

    }

    @Bean
    public ICommandHandler<GetAllOrderCmd> getAllOrderCmdICommandHandler() {

        return executionContext -> {

            GetAllOrderCmd cmd = executionContext.getCommand();

            List<Order> orderList;
            orderList = orderRepository.findAll();

            cmd.setOrderList(orderList);

            cmd.setResponse(new CreateEntityResp(cmd.getOrderList().get(0).getOrderNumber()));
        };

    }

    public boolean checkCurrentStock(String StockID) {
        return true;
    }
}

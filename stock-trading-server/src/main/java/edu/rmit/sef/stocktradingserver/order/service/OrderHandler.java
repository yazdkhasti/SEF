package edu.rmit.sef.stocktradingserver.order.service;

import edu.rmit.command.core.CommandUtil;
import edu.rmit.command.core.ICommandHandler;
import edu.rmit.command.core.ICommandService;
import edu.rmit.command.core.InitCmd;
import edu.rmit.sef.core.model.Entity;
import edu.rmit.sef.order.command.*;
import edu.rmit.sef.core.command.CreateEntityResp;
import edu.rmit.sef.order.model.Order;
import edu.rmit.sef.order.model.OrderState;
import edu.rmit.sef.order.model.OrderType;
import edu.rmit.sef.portfolio.command.GetUserStockPortfolioCmd;
import edu.rmit.sef.portfolio.command.GetUserStockPortfolioResp;
import edu.rmit.sef.portfolio.model.StockPortfolio;
import edu.rmit.sef.stock.command.FindStockByIdCmd;
import edu.rmit.sef.stock.command.FindStockByIdResp;
import edu.rmit.sef.stock.model.Stock;
import edu.rmit.sef.stocktradingserver.order.repo.OrderRepository;
import edu.rmit.sef.stocktradingserver.portfolio.command.UpdateUserStockPortfolioCmd;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
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

    @Autowired
    private MongoTemplate db;

    @Value("${edu.rmit.sef.stocktrading.server.order.transactionIdSeed}")
    long transactionIdSeed;

    @Value("${edu.rmit.sef.stocktrading.server.order.orderPriceThreshold}")
    double orderPriceThreshold;


    @Bean
    public ICommandHandler<InitCmd> initCmdICommandHandler() {

        return executionContext -> {

            long orderCount = orderRepository.count();
            lastOrderNumber.set(transactionIdSeed + orderCount);

        };
    }


    @Bean
    public ICommandHandler<CreateOrderCmd> createOrderHandler() {

        return executionContext -> {

            CreateOrderCmd cmd = executionContext.getCommand();

            ICommandService commandService = executionContext.getCommandService();

            Order order = Entity.newEntity(executionContext.getUserId(), Order.class);

            modelMapper.getConfiguration().setAmbiguityIgnored(true);
            modelMapper.map(cmd, order);

            FindStockByIdCmd findStockByIdCmd = new FindStockByIdCmd();
            findStockByIdCmd.setId(cmd.getStockId());

            FindStockByIdResp findStockByIdResp = commandService
                    .execute(findStockByIdCmd)
                    .join();


            Stock stock = findStockByIdResp.getStock();

            double orderPrice = order.getPrice();
            double maxValue = stock.getPrice() + orderPriceThreshold;
            double minValue = stock.getPrice() - orderPriceThreshold;

            if (orderPrice < maxValue && orderPrice > minValue) {
                CommandUtil.throwAppExecutionException("Buy/Sell orders must be within +/-10 cents of the last trade.");
            }

            GetUserStockPortfolioCmd getUserStockPortfolioCmd = new GetUserStockPortfolioCmd();
            getUserStockPortfolioCmd.setStockId(cmd.getStockId());
            getUserStockPortfolioCmd.setUserId(executionContext.getUserId());

            GetUserStockPortfolioResp getUserStockPortfolioResp = commandService
                    .execute(getUserStockPortfolioCmd)
                    .join();

            StockPortfolio stockPortfolio = getUserStockPortfolioResp.getStockPortfolio();

            if (order.getOrderType() == OrderType.Sell && order.getQuantity() > stockPortfolio.getQuantity()) {
                CommandUtil.throwAppExecutionException("Client does not own the quantity of stock specified");
            }


            UpdateUserStockPortfolioCmd updateUserStockPortfolioCmd = new UpdateUserStockPortfolioCmd();
            updateUserStockPortfolioCmd.setStockId(cmd.getStockId());
            updateUserStockPortfolioCmd.setUserId(executionContext.getUserId());

            long quantityChanged = order.getQuantity();
            if (order.getOrderType() == OrderType.Sell) {
                quantityChanged *= -1;
            }
            updateUserStockPortfolioCmd.setQuantityChanged(quantityChanged);


            commandService.execute(updateUserStockPortfolioCmd);

            Long orderNumber = lastOrderNumber.getAndDecrement();
            String transactionId = Order.getTransactionId(orderNumber);
            order.setTransactionId(transactionId);
            order.setOrderState(OrderState.PendingTrade);

            orderRepository.insert(order);

            cmd.setResponse(new CreateEntityResp(order.getTransactionId()));


        };

    }


    @Bean
    public ICommandHandler<GetAllOrderCmd> getAllOrderCmdICommandHandler() {

        return executionContext -> {

            GetAllOrderCmd cmd = executionContext.getCommand();

            List<Order> orderList;

            Criteria criteria = Criteria.where("CreatedBy").regex(executionContext.getUserId(), "i");

            Query query = Query.query(criteria);
            long orderCount = db.count(query,Order.class);
            query.with(cmd.toPageable());
            orderList = db.find(query, Order.class);

            GetAllOrderResp resp = new GetAllOrderResp();
            resp.setOrderList(orderList);
            resp.setTotalCount(orderCount);

            cmd.setResponse(resp);
        };

    }


}

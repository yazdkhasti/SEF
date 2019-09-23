package edu.rmit.sef.stocktradingserver.order.service;

import edu.rmit.command.core.CommandUtil;
import edu.rmit.command.core.ICommandHandler;
import edu.rmit.command.core.ICommandService;
import edu.rmit.command.core.InitCmd;
import edu.rmit.sef.core.command.CreateEntityResp;
import edu.rmit.sef.core.model.Entity;
import edu.rmit.sef.order.command.*;
import edu.rmit.sef.order.model.Order;
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
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;
import java.util.Optional;
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

    @Value("${edu.rmit.sef.stocktrading.server.order.orderQuantityThreshold}")
    long orderQuantityThreshold;


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

            CommandUtil.must(() -> cmd.getQuantity() <= orderQuantityThreshold,
                    "Quantity bought or sold cannot exceed " + orderQuantityThreshold + " for each order");

            ICommandService commandService = executionContext.getCommandService();

            Order order = Entity.newEntity(executionContext.getUserId(), Order.class);
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


            if (orderPrice <= maxValue && orderPrice >= minValue)
                CommandUtil.throwAppExecutionException("Buy/Sell orders must be within +/-10 cents of the last trade.");


            if (order.getOrderType() == OrderType.Sell) {

                GetUserStockPortfolioCmd getUserStockPortfolioCmd = new GetUserStockPortfolioCmd();
                getUserStockPortfolioCmd.setStockId(cmd.getStockId());
                getUserStockPortfolioCmd.setUserId(executionContext.getUserId());

                GetUserStockPortfolioResp getUserStockPortfolioResp = commandService
                        .execute(getUserStockPortfolioCmd)
                        .join();

                StockPortfolio stockPortfolio = getUserStockPortfolioResp.getStockPortfolio();

                CommandUtil.must(() -> order.getQuantity() <= stockPortfolio.getQuantity(), "Client does not own the quantity of stock specified");

                UpdateUserStockPortfolioCmd updateUserStockPortfolioCmd = new UpdateUserStockPortfolioCmd();
                updateUserStockPortfolioCmd.setStockId(cmd.getStockId());
                updateUserStockPortfolioCmd.setUserId(executionContext.getUserId());

                long quantityChanged = -order.getQuantity();

                updateUserStockPortfolioCmd.setQuantityChanged(quantityChanged);

                commandService.execute(updateUserStockPortfolioCmd).join();

            }


            long orderNumber = lastOrderNumber.getAndDecrement();
            String transactionId = Order.formatTransactionId(orderNumber);
            order.setTransactionId(transactionId);


            orderRepository.insert(order);


            cmd.setResponse(new CreateEntityResp(order.getId()));




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

    @Bean
    public ICommandHandler<FindOrderByIdCmd> findOrderByIdHandler() {

        return executionContext -> {

            FindOrderByIdCmd cmd = executionContext.getCommand();
            Optional<Order> optionalOrder = orderRepository.findById(cmd.getOrderId());

            CommandUtil.must(() -> optionalOrder.isPresent(), "Order with the id" + cmd.getOrderId() + " not found.");

            FindOrderByIdResp resp = new FindOrderByIdResp();
            resp.setOrder(optionalOrder.get());

            cmd.setResponse(resp);

        };

    }


}

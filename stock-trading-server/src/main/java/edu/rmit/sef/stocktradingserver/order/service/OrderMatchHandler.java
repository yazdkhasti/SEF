package edu.rmit.sef.stocktradingserver.order.service;

import edu.rmit.command.core.*;
import edu.rmit.sef.core.command.PublishEventCmd;
import edu.rmit.sef.core.security.Authority;
import edu.rmit.sef.order.command.CreateOrderCmd;
import edu.rmit.sef.order.command.GetOrderTradeTransactionsCmd;
import edu.rmit.sef.order.command.GetOrderTradeTransactionsResp;
import edu.rmit.sef.order.command.WithdrawOrderCmd;
import edu.rmit.sef.order.model.*;
import edu.rmit.sef.stock.command.UpdateStockPriceCmd;
import edu.rmit.sef.stocktradingserver.core.security.SecurityUtil;
import edu.rmit.sef.stocktradingserver.order.command.MatchOrderCmd;
import edu.rmit.sef.stocktradingserver.order.command.OrderExeutionParameters;
import edu.rmit.sef.stocktradingserver.order.command.OrderMatchedCmd;
import edu.rmit.sef.stocktradingserver.order.repo.OrderLineTransactionRepository;
import edu.rmit.sef.stocktradingserver.portfolio.command.UpdateUserStockPortfolioCmd;
import edu.rmit.sef.user.model.SystemUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Configuration
public class OrderMatchHandler {

    @Autowired
    MongoTemplate db;


    @Autowired
    OrderLineTransactionRepository orderLineTransactionRepository;

    @Autowired
    private SecurityUtil securityUtil;

    private enum MatchType {
        Exact,
        GreatestPrice,
        LowestPrice
    }


    @Bean
    public IQueueKeySelector<MatchOrderCmd> matchOrderQueueKeySelector() {

        return (command, tClass) -> {
            Order order = db.findById(command.getOrderId(), Order.class);
            return this.getClass().getName() + order.getStockId();
        };

    }

    @Bean
    public IQueueKeySelector<WithdrawOrderCmd> withdrawOrderQueueKeySelector() {


        return (command, tClass) -> {

            Order order = db.findById(command.getOrderId(), Order.class);
            return this.getClass().getName() + order.getStockId();

        };

    }

    @Bean
    public ICommandHandler<WithdrawOrderCmd> withdrawOrderHandler() {
        return executionContext -> {

            WithdrawOrderCmd cmd = executionContext.getCommand();

            Order order = db.findById(cmd.getOrderId(), Order.class);

            String userId = executionContext.getUserId();

            CommandUtil.must(() -> order.getCreatedBy().compareTo(userId) == 0 || securityUtil.hasAuthority(userId, Authority.ADMIN),
                    "The current user is not authorized to withdraw the order");

            order.withdraw();
            order.update(executionContext.getUserId());

            db.save(order);


            if (order.getOrderType() == OrderType.Sell) {

                UpdateUserStockPortfolioCmd updateUserStockPortfolioCmd = new UpdateUserStockPortfolioCmd();
                updateUserStockPortfolioCmd.setQuantityChanged(order.getRemainedQuantity());
                updateUserStockPortfolioCmd.setStockId(order.getStockId());
                updateUserStockPortfolioCmd.setUserId(order.getCreatedBy());
                executionContext.getCommandService().execute(updateUserStockPortfolioCmd).join();

            }

            cmd.setResponse(new NullResp());

        };
    }

    @Bean
    ICommandPostHandler<CreateOrderCmd> createOrderPostHandler() {
        return executionContext -> {

            CreateOrderCmd createOrderCmd = executionContext.getCommand();
            String orderId = createOrderCmd.getResponse().getId();
            ICommandService commandService = executionContext.getCommandService(SystemUser.SYSTEM_USER_ID);

            Order order = db.findById(orderId, Order.class);

            if (order.getOrderType() == OrderType.Sell) {

                UpdateUserStockPortfolioCmd updateUserStockPortfolioCmd = new UpdateUserStockPortfolioCmd();
                updateUserStockPortfolioCmd.setStockId(order.getStockId());
                updateUserStockPortfolioCmd.setUserId(executionContext.getUserId());

                long quantityChanged = -order.getQuantity();

                updateUserStockPortfolioCmd.setQuantityChanged(quantityChanged);

                commandService.execute(updateUserStockPortfolioCmd).join();

            }

            MatchOrderCmd matchOrderCmd = new MatchOrderCmd();
            matchOrderCmd.setOrderId(orderId);
            commandService.execute(matchOrderCmd);


        };
    }

    @Bean
    public ICommandHandler<OrderMatchedCmd> orderMatchedHandler() {
        return executionContext -> {

            OrderMatchedCmd cmd = executionContext.getCommand();
            ICommandService commandService = executionContext.getCommandService();


            Order buyerOrder = cmd.getBuyOrder();

            UpdateUserStockPortfolioCmd updateUserStockPortfolioCmd = new UpdateUserStockPortfolioCmd();
            updateUserStockPortfolioCmd.setUserId(buyerOrder.getCreatedBy());
            updateUserStockPortfolioCmd.setStockId(cmd.getStockId());
            updateUserStockPortfolioCmd.setQuantityChanged(cmd.getTradeQuantity());
            commandService.execute(updateUserStockPortfolioCmd);

            Order sellOrder = cmd.getSellOrder();

            TradeTransaction masterTransaction = new TradeTransaction();
            masterTransaction.setMasterTransactionId(UUID.randomUUID().toString());
            masterTransaction.setBuyerOrderId(buyerOrder.getId());
            masterTransaction.setSellerOrderId(sellOrder.getId());
            masterTransaction.setBuyerOrderLineTransactionId(cmd.getBuyLine().getOrderLineTransactionId());
            masterTransaction.setSellerOrderLineTransactionId(cmd.getSellLine().getOrderLineTransactionId());
            masterTransaction.setTradeQuantity(cmd.getTradeQuantity());
            masterTransaction.setExecutedPrice(cmd.getExecutedPrice());
            masterTransaction.setExecutedOn(cmd.getExecutedOn());
            db.save(masterTransaction);


            OrderMatchedEvent buyerEvent = new OrderMatchedEvent(buyerOrder.getTransactionId(), buyerOrder.getStockId(), cmd.getTradeQuantity(), cmd.getExecutedOn());
            commandService.execute(new PublishEventCmd(buyerEvent, OrderEventNames.ORDER_MATCHED, buyerOrder.getCreatedBy()));

            OrderMatchedEvent sellerEvent = new OrderMatchedEvent(sellOrder.getTransactionId(), sellOrder.getStockId(), cmd.getTradeQuantity(), cmd.getExecutedOn());
            commandService.execute(new PublishEventCmd(sellerEvent, OrderEventNames.ORDER_MATCHED, sellOrder.getCreatedBy()));


            UpdateStockPriceCmd updateStockCmd = new UpdateStockPriceCmd();
            updateStockCmd.setPrice(cmd.getExecutedPrice());
            updateStockCmd.setStockId(cmd.getStockId());
            commandService.execute(updateStockCmd);

            cmd.setResponse(new NullResp());

        };
    }


    @Bean
    public ICommandHandler<MatchOrderCmd> matchOrderHandler() {

        return executionContext -> {


            MatchOrderCmd cmd = executionContext.getCommand();

            if (!executionContext.getExecutionParameter(OrderExeutionParameters.DISABLE_ORDER_MATCH, false)) {


                ICommandService commandService = executionContext.getCommandService();


                Order order = db.findById(cmd.getOrderId(), Order.class);


                CommandUtil.must(() -> order.validForTrade(), "Order is not in a valid state for trade.");


                boolean continueMatchingFlag = true;

                do {

                    Order matchedOrder = null;


                    if (order.getOrderType() == OrderType.Sell) {

                        matchedOrder = getOrderFromQueue(order.getStockId(), OrderType.Buy, order.getPrice(), MatchType.Exact);

                        if (matchedOrder == null) {

                            matchedOrder = getOrderFromQueue(order.getStockId(), OrderType.Buy, order.getPrice(), MatchType.GreatestPrice);

                        }

                    } else {

                        matchedOrder = getOrderFromQueue(order.getStockId(), OrderType.Sell, order.getPrice(), MatchType.Exact);

                        if (matchedOrder == null) {

                            matchedOrder = getOrderFromQueue(order.getStockId(), OrderType.Sell, order.getPrice(), MatchType.LowestPrice);

                        }

                    }

                    if (matchedOrder != null) {

                        double price = Math.min(order.getPrice(), matchedOrder.getPrice());

                        matchOrders(commandService, order, matchedOrder, price);

                    } else {

                        continueMatchingFlag = false;

                    }

                } while (continueMatchingFlag && order.validForTrade());

            }

            cmd.setResponse(new NullResp());

        };

    }

    @Bean
    public ICommandHandler<GetOrderTradeTransactionsCmd> getOrderTradeTransactionsHandler() {

        return executionContext -> {

            GetOrderTradeTransactionsCmd cmd = executionContext.getCommand();

            Order order = db.findById(cmd.getOrderId(), Order.class);

            List<TradeTransaction> tradeTransactions = getOrderTradeTransactions(order.getId(), order.getOrderType());

            GetOrderTradeTransactionsResp resp = new GetOrderTradeTransactionsResp();
            resp.setTradeTransactions(tradeTransactions);

            cmd.setResponse(resp);
        };

    }


    private void matchOrders(ICommandService commandService, Order order, Order matchedOrder, double price) {

        Order buyOrder, sellOrder;

        if (order.getOrderType() == OrderType.Buy) {
            buyOrder = order;
            sellOrder = matchedOrder;
        } else {
            buyOrder = matchedOrder;
            sellOrder = order;
        }

        long tradeQuantity = Math.min(buyOrder.getRemainedQuantity(), sellOrder.getRemainedQuantity());
        Date executedOn = new Date();

        if (buyOrder.getStockId().compareTo(sellOrder.getStockId()) != 0) {
            CommandUtil.throwAppExecutionException("Buy order stock and sell order stock must be same.");
        }

        OrderLineTransaction butLine = buyOrder.trade(tradeQuantity, price, executedOn);

        OrderLineTransaction sellLine = sellOrder.trade(tradeQuantity, price, executedOn);

        db.save(buyOrder);

        db.save(sellOrder);

        commandService.execute(new OrderMatchedCmd(buyOrder, butLine, sellOrder, sellLine, buyOrder.getStockId(), tradeQuantity, price));
    }

    private Order getOrderFromQueue(String stockId, OrderType orderType, double price, MatchType matchType) {

        Criteria criteria;

        if (matchType == MatchType.Exact) {
            criteria = Criteria.where("price").is(price);
        } else if (matchType == MatchType.GreatestPrice) {
            criteria = Criteria.where("price").gte(price);
        } else {
            criteria = Criteria.where("price").lte(price);
        }


        criteria = criteria.andOperator(
                Criteria.where("orderType").is(orderType),
                Criteria.where("stockId").is(stockId),
                Criteria.where("orderState").in(OrderState.PendingTrade, OrderState.PartiallyTraded)
        );

        Query query = Query.query(criteria);

        if (matchType == MatchType.GreatestPrice) {
            query = query.with(Sort.by(Sort.Direction.DESC, "price"));
        } else if ((matchType == MatchType.LowestPrice)) {
            query = query.with(Sort.by(Sort.Direction.ASC, "price"));
        }

        query = query.with(Sort.by(Sort.Direction.ASC, "createdOn"));

        return db.findOne(query, Order.class);
    }

    private List<TradeTransaction> getOrderTradeTransactions(String orderId, OrderType orderType) {

        Criteria criteria;

        if (orderType == OrderType.Buy) {
            criteria = Criteria.where("buyerOrderId").is(orderId);
        } else {
            criteria = Criteria.where("sellerOrderId").is(orderId);
        }


        Query query = Query.query(criteria);

        return db.find(query, TradeTransaction.class);
    }


}

package edu.rmit.sef.stocktradingserver.order.service;

import edu.rmit.command.core.*;
import edu.rmit.sef.core.command.PublishEventCmd;
import edu.rmit.sef.order.command.WithdrawOrderCmd;
import edu.rmit.sef.order.model.*;
import edu.rmit.sef.stock.command.UpdateStockCmd;
import edu.rmit.sef.stock.command.UpdateStockPriceCmd;
import edu.rmit.sef.stocktradingserver.order.command.MatchOrderCmd;
import edu.rmit.sef.stocktradingserver.order.command.OrderMatchedCmd;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.UUID;

@Configuration
public class OrderMatchHandler {

    @Autowired
    MongoTemplate db;

    private enum MatchType {
        Exact,
        GreatestPrice,
        LowestPrice
    }


    @Bean
    public IQueueKeySelector<MatchOrderCmd> matchOrderQueueKeySelector() {

        return (command, tClass) -> tClass.getName() + command.getOrder().getStockId() + command.getOrder().getOrderType();

    }

    @Bean
    public IQueueKeySelector<WithdrawOrderCmd> withdrawOrderQueueKeySelector() {


        return (command, tClass) -> {

            Order order = db.findById(command.getOrderId(), Order.class);
            return tClass.getName() + order.getStockId() + order.getOrderType();

        };

    }

    @Bean
    public ICommandHandler<WithdrawOrderCmd> withdrawOrderHandler() {
        return executionContext -> {

            WithdrawOrderCmd cmd = executionContext.getCommand();

            Order order = db.findById(cmd.getOrderId(), Order.class);
            order.withdraw();

            db.save(order);
        };
    }


    @Bean
    public ICommandHandler<OrderMatchedCmd> orderMatchedHandler() {
        return executionContext -> {

            OrderMatchedCmd cmd = executionContext.getCommand();
            ICommandService commandService = executionContext.getCommandService();

            Order buyerOrder = cmd.getBuyOrder();
            OrderLineTransaction buyOrderLineTransaction = new OrderLineTransaction(buyerOrder.getId(), cmd.getTradeQuantity(), UUID.randomUUID().toString(), cmd.getExecutedPrice(), cmd.getExecutedOn());
            db.save(buyOrderLineTransaction);

            Order sellOrder = cmd.getSellOrder();
            OrderLineTransaction sellOrderLineTransaction = new OrderLineTransaction(sellOrder.getId(), cmd.getTradeQuantity(), UUID.randomUUID().toString(), cmd.getExecutedPrice(), cmd.getExecutedOn());
            db.save(sellOrderLineTransaction);

            TradeTransaction masterTransaction = new TradeTransaction();
            masterTransaction.setMasterTransactionId(UUID.randomUUID().toString());
            masterTransaction.setBuyerOrderId(buyerOrder.getId());
            masterTransaction.setSellerOrderId(sellOrder.getId());
            masterTransaction.setBuyerOrderLineTransactionId(buyOrderLineTransaction.getOrderLineTransactionId());
            masterTransaction.setBuyerOrderLineTransactionId(sellOrderLineTransaction.getOrderLineTransactionId());
            masterTransaction.setTradeQuantity(cmd.getTradeQuantity());
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
            Order order = cmd.getOrder();
            ICommandService commandService = executionContext.getCommandService();


            boolean continueMatchingFlag = true;

            if (order.getOrderType() == OrderType.Sell) {


                do {

                    Order buyOrder = getOrderFromQueue(order.getStockId(), OrderType.Buy, order.getPrice(), MatchType.Exact);

                    if (buyOrder != null) {

                        commandService.execute(matchOrders(buyOrder, order, buyOrder.getPrice()));

                    } else {

                        buyOrder = getOrderFromQueue(order.getStockId(), OrderType.Buy, order.getPrice(), MatchType.GreatestPrice);

                        if (buyOrder == null) {

                            continueMatchingFlag = false;

                        } else {
                            commandService.execute(matchOrders(buyOrder, order, buyOrder.getPrice()));
                        }
                    }

                } while (continueMatchingFlag);

            } else {

                do {

                    Order sellOrder = getOrderFromQueue(order.getStockId(), OrderType.Sell, order.getPrice(), MatchType.Exact);

                    if (sellOrder != null) {

                        commandService.execute(matchOrders(order, sellOrder, sellOrder.getPrice()));

                    } else {

                        sellOrder = getOrderFromQueue(order.getStockId(), OrderType.Sell, order.getPrice(), MatchType.LowestPrice);

                        if (sellOrder == null) {

                            continueMatchingFlag = false;

                        } else {

                            commandService.execute(matchOrders(order, sellOrder, sellOrder.getPrice()));

                        }
                    }
                } while (continueMatchingFlag);
            }

            cmd.setResponse(new NullResp());

        };

    }


    private OrderMatchedCmd matchOrders(Order buyOrder, Order sellOrder, double price) {

        int tradeQuantity = Math.min(buyOrder.getRemainedQuantity(), sellOrder.getRemainedQuantity());

        if (buyOrder.getStockId() != sellOrder.getStockId()) {
            CommandUtil.throwAppExecutionException("Buy order stock and sell order stock must be same.");
        }

        buyOrder.trade(tradeQuantity);

        sellOrder.trade(tradeQuantity);

        db.save(buyOrder);

        db.save(sellOrder);

        return new OrderMatchedCmd(buyOrder, sellOrder, buyOrder.getStockId(), tradeQuantity, price);
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

        criteria = criteria.andOperator(Criteria.where("orderType").is(orderType));

        criteria = criteria.andOperator(Criteria.where("stockId").is(stockId));

        criteria = criteria.andOperator(Criteria.where("orderState").in(OrderState.PendingTrade, OrderState.PartiallyTraded));

        Query query = Query.query(criteria).with(Sort.by(Sort.Direction.ASC, "createdOn"));

        return db.findOne(query, Order.class);
    }
}

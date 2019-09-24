package edu.rmit.sef.stocktradingserver.order;

import edu.rmit.command.core.ExecutionOptions;
import edu.rmit.command.core.ICommandService;
import edu.rmit.command.exception.CommandExecutionException;
import edu.rmit.sef.order.command.*;
import edu.rmit.sef.order.model.OrderState;
import edu.rmit.sef.order.model.OrderType;
import edu.rmit.sef.order.model.TradeTransaction;
import edu.rmit.sef.portfolio.command.GetUserStockPortfolioCmd;
import edu.rmit.sef.portfolio.command.GetUserStockPortfolioResp;
import edu.rmit.sef.stock.command.FindStockByIdCmd;
import edu.rmit.sef.stock.command.FindStockByIdResp;
import edu.rmit.sef.stocktradingserver.core.BaseTest;
import edu.rmit.sef.stocktradingserver.order.command.MatchOrderCmd;
import edu.rmit.sef.stocktradingserver.order.command.OrderExeutionParameters;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class OrderMatchTests extends BaseTest {


    @Test
    public void OrdersWithExactPriceMatchTest() {

        ICommandService commandService = getSystemCommandService();

        String stockId = addStock(300.5);
        String firstUserId = addUser();
        String secondUserId = addUser();

        ExecutionOptions executionOptions = new ExecutionOptions();
        executionOptions.addExecutionParameter(OrderExeutionParameters.DISABLE_ORDER_MATCH, true);

        String buyOrderId = addOrder(firstUserId, stockId, 20, 300.6, OrderType.Buy, executionOptions);

        addPortfolio(secondUserId, stockId, 20);
        String sellOrderId = addOrder(secondUserId, stockId, 20, 300.6, OrderType.Sell, executionOptions);


        FindOrderByIdCmd findBuyOrderByIdCmd = new FindOrderByIdCmd();
        findBuyOrderByIdCmd.setOrderId(buyOrderId);
        FindOrderByIdResp findBuyOrderByIdResp = commandService.execute(findBuyOrderByIdCmd).join();

        Assert.assertEquals(findBuyOrderByIdResp.getOrder().getOrderState(), OrderState.PendingTrade);

        FindOrderByIdCmd findSellOrderByIdCmd = new FindOrderByIdCmd();
        findSellOrderByIdCmd.setOrderId(sellOrderId);
        FindOrderByIdResp findSellOrderByIdResp = commandService.execute(findSellOrderByIdCmd).join();

        Assert.assertEquals(findSellOrderByIdResp.getOrder().getOrderState(), OrderState.PendingTrade);


        MatchOrderCmd matchBuyOrderCmd = new MatchOrderCmd();
        matchBuyOrderCmd.setOrderId(buyOrderId);
        commandService.execute(matchBuyOrderCmd).join();

        waitForAllTasks();


        FindOrderByIdResp findBuyOrderByIdAfterMatchResp = commandService.execute(findBuyOrderByIdCmd).join();
        FindOrderByIdResp findSellOrderByIdAfterMatchResp = commandService.execute(findSellOrderByIdCmd).join();


        Assert.assertEquals(findBuyOrderByIdAfterMatchResp.getOrder().getOrderState(), OrderState.TradedCompletely);
        Assert.assertEquals(findSellOrderByIdAfterMatchResp.getOrder().getOrderState(), OrderState.TradedCompletely);


    }

    @Test
    public void OrdersWithDifferentPriceMatchTest() {

        ICommandService commandService = getCommandService();

        String stockId = addStock(300.5);
        String firstUserId = addUser();
        String secondUserId = addUser();

        ExecutionOptions executionOptions = new ExecutionOptions();

        String buyOrderId = addOrder(firstUserId, stockId, 20, 300.6, OrderType.Buy, executionOptions);

        addPortfolio(secondUserId, stockId, 20);
        String sellOrderId = addOrder(secondUserId, stockId, 20, 300.55, OrderType.Sell, executionOptions);


        waitForAllTasks();


        FindOrderByIdCmd findBuyOrderByIdCmd = new FindOrderByIdCmd();
        findBuyOrderByIdCmd.setOrderId(buyOrderId);
        FindOrderByIdResp findBuyOrderByIdResp = commandService.execute(findBuyOrderByIdCmd).join();

        FindOrderByIdCmd findSellOrderByIdCmd = new FindOrderByIdCmd();
        findSellOrderByIdCmd.setOrderId(sellOrderId);
        FindOrderByIdResp findSellOrderByIdResp = commandService.execute(findSellOrderByIdCmd).join();


        Assert.assertEquals(findBuyOrderByIdResp.getOrder().getOrderState(), OrderState.TradedCompletely);
        Assert.assertEquals(findSellOrderByIdResp.getOrder().getOrderState(), OrderState.TradedCompletely);

        GetOrderTradeTransactionsCmd getOrderTradeTransactionsCmd = new GetOrderTradeTransactionsCmd();
        getOrderTradeTransactionsCmd.setOrderId(buyOrderId);
        GetOrderTradeTransactionsResp getOrderTradeTransactionsResp = commandService.execute(getOrderTradeTransactionsCmd).join();

        Assert.assertEquals(getOrderTradeTransactionsResp.getTradeTransactions().size(), 1);

        TradeTransaction tradeTransaction = getOrderTradeTransactionsResp.getTradeTransactions().get(0);

        Assert.assertEquals(tradeTransaction.getExecutedPrice(), 300.55, 0);

    }

    @Test
    public void MultipleSellOrderMatchTest() {

        ICommandService commandService = getCommandService();

        String stockId = addStock(300.5);
        String firstUserId = addUser();
        String secondUserId = addUser();
        String thirdUserId = addUser();

        ExecutionOptions executionOptions = new ExecutionOptions();

        addPortfolio(secondUserId, stockId, 400);
        String sellOrder1Id = addOrder(secondUserId, stockId, 400, 300.56, OrderType.Sell, executionOptions);

        addPortfolio(thirdUserId, stockId, 100);
        String sellOrder2Id = addOrder(thirdUserId, stockId, 100, 300.52, OrderType.Sell, executionOptions);

        String buyOrderId = addOrder(firstUserId, stockId, 400, 300.6, OrderType.Buy, executionOptions);


        waitForAllTasks();


        FindOrderByIdCmd findBuyOrderByIdCmd = new FindOrderByIdCmd();
        findBuyOrderByIdCmd.setOrderId(buyOrderId);
        FindOrderByIdResp findBuyOrderByIdResp = commandService.execute(findBuyOrderByIdCmd).join();

        FindOrderByIdCmd findSellOrder1ByIdCmd = new FindOrderByIdCmd();
        findSellOrder1ByIdCmd.setOrderId(sellOrder1Id);
        FindOrderByIdResp findSellOrder1ByIdResp = commandService.execute(findSellOrder1ByIdCmd).join();

        FindOrderByIdCmd findSellOrder2ByIdCmd = new FindOrderByIdCmd();
        findSellOrder2ByIdCmd.setOrderId(sellOrder2Id);
        FindOrderByIdResp findSellOrder2ByIdResp = commandService.execute(findSellOrder2ByIdCmd).join();


        Assert.assertEquals(findBuyOrderByIdResp.getOrder().getOrderState(), OrderState.TradedCompletely);
        Assert.assertEquals(findSellOrder1ByIdResp.getOrder().getOrderState(), OrderState.PartiallyTraded);
        Assert.assertEquals(findSellOrder1ByIdResp.getOrder().getRemainedQuantity(), 100);
        Assert.assertEquals(findSellOrder2ByIdResp.getOrder().getOrderState(), OrderState.TradedCompletely);

        GetOrderTradeTransactionsCmd getOrderTradeTransactionsCmd = new GetOrderTradeTransactionsCmd();
        getOrderTradeTransactionsCmd.setOrderId(buyOrderId);
        GetOrderTradeTransactionsResp getOrderTradeTransactionsResp = commandService.execute(getOrderTradeTransactionsCmd).join();

        Assert.assertEquals(getOrderTradeTransactionsResp.getTradeTransactions().size(), 2);

        TradeTransaction tradeTransaction1 = getOrderTradeTransactionsResp.getTradeTransactions().get(0);
        TradeTransaction tradeTransaction2 = getOrderTradeTransactionsResp.getTradeTransactions().get(1);

        Assert.assertEquals(tradeTransaction1.getExecutedPrice(), 300.52, 0);
        Assert.assertEquals(tradeTransaction2.getExecutedPrice(), 300.56, 0);

    }

    @Test
    public void MultipleBuyOrderMatchTest() {

        ICommandService commandService = getCommandService();

        String stockId = addStock(300.5);
        String firstUserId = addUser();
        String secondUserId = addUser();
        String thirdUserId = addUser();

        ExecutionOptions executionOptions = new ExecutionOptions();


        String buyOrder1Id = addOrder(secondUserId, stockId, 100, 300.58, OrderType.Buy, executionOptions);

        String buyOrder2Id = addOrder(thirdUserId, stockId, 300, 300.6, OrderType.Buy, executionOptions);

        addPortfolio(firstUserId, stockId, 300);
        String sellOrderId = addOrder(firstUserId, stockId, 300, 300.56, OrderType.Sell, executionOptions);


        waitForAllTasks();

        FindOrderByIdCmd findSellOrderByIdCmd = new FindOrderByIdCmd();
        findSellOrderByIdCmd.setOrderId(sellOrderId);
        FindOrderByIdResp findSellOrderByIdResp = commandService.execute(findSellOrderByIdCmd).join();


        FindOrderByIdCmd findBuyOrder1ByIdCmd = new FindOrderByIdCmd();
        findBuyOrder1ByIdCmd.setOrderId(buyOrder1Id);
        FindOrderByIdResp findBuyOrder1ByIdResp = commandService.execute(findBuyOrder1ByIdCmd).join();


        FindOrderByIdCmd findBuyOrder2ByIdCmd = new FindOrderByIdCmd();
        findBuyOrder2ByIdCmd.setOrderId(buyOrder2Id);
        FindOrderByIdResp findBuyOrder2ByIdResp = commandService.execute(findBuyOrder2ByIdCmd).join();


        Assert.assertEquals(findSellOrderByIdResp.getOrder().getOrderState(), OrderState.TradedCompletely);
        Assert.assertEquals(findBuyOrder1ByIdResp.getOrder().getOrderState(), OrderState.PendingTrade);
        Assert.assertEquals(findBuyOrder1ByIdResp.getOrder().getRemainedQuantity(), 100);
        Assert.assertEquals(findBuyOrder2ByIdResp.getOrder().getOrderState(), OrderState.TradedCompletely);

        GetOrderTradeTransactionsCmd getOrderTradeTransactionsCmd = new GetOrderTradeTransactionsCmd();
        getOrderTradeTransactionsCmd.setOrderId(sellOrderId);
        GetOrderTradeTransactionsResp getOrderTradeTransactionsResp = commandService.execute(getOrderTradeTransactionsCmd).join();

        Assert.assertEquals(getOrderTradeTransactionsResp.getTradeTransactions().size(), 1);

        TradeTransaction tradeTransaction1 = getOrderTradeTransactionsResp.getTradeTransactions().get(0);

        Assert.assertEquals(tradeTransaction1.getExecutedPrice(), 300.56, 0);

    }

    @Test(expected = CommandExecutionException.class)
    public void CompletedOrderMatchTest() {
        ICommandService commandService = getCommandService();

        String stockId = addStock(300.5);
        String firstUserId = addUser();
        String secondUserId = addUser();

        ExecutionOptions executionOptions = new ExecutionOptions();


        String buyOrderId = addOrder(firstUserId, stockId, 20, 300.6, OrderType.Buy, executionOptions);

        addPortfolio(secondUserId, stockId, 20);
        String sellOrderId = addOrder(secondUserId, stockId, 20, 300.6, OrderType.Sell, executionOptions);


        waitForAllTasks();

        FindOrderByIdCmd findBuyOrderByIdCmd = new FindOrderByIdCmd();
        findBuyOrderByIdCmd.setOrderId(buyOrderId);
        FindOrderByIdResp findBuyOrderByIdAfterMatchResp = commandService.execute(findBuyOrderByIdCmd).join();

        FindOrderByIdCmd findSellOrderByIdCmd = new FindOrderByIdCmd();
        findSellOrderByIdCmd.setOrderId(sellOrderId);
        FindOrderByIdResp findSellOrderByIdAfterMatchResp = commandService.execute(findSellOrderByIdCmd).join();


        Assert.assertEquals(findBuyOrderByIdAfterMatchResp.getOrder().getOrderState(), OrderState.TradedCompletely);
        Assert.assertEquals(findBuyOrderByIdAfterMatchResp.getOrder().getRemainedQuantity(), 0);

        Assert.assertEquals(findSellOrderByIdAfterMatchResp.getOrder().getOrderState(), OrderState.TradedCompletely);
        Assert.assertEquals(findSellOrderByIdAfterMatchResp.getOrder().getRemainedQuantity(), 0);

        //Match again
        MatchOrderCmd matchBuyOrderCmd = new MatchOrderCmd();
        matchBuyOrderCmd.setOrderId(buyOrderId);
        commandService.execute(matchBuyOrderCmd).join();

        waitForAsyncTasks();


    }

    @Test
    public void PartiallyTradableOrderMatchTest() {
        ICommandService commandService = getCommandService();

        String stockId = addStock(300.5);
        String firstUserId = addUser();
        String secondUserId = addUser();

        ExecutionOptions executionOptions = new ExecutionOptions();

        String buyOrderId = addOrder(firstUserId, stockId, 20, 300.6, OrderType.Buy, executionOptions);

        addPortfolio(secondUserId, stockId, 30);
        String sellOrderId = addOrder(secondUserId, stockId, 30, 300.6, OrderType.Sell, executionOptions);


        waitForAllTasks();

        FindOrderByIdCmd findBuyOrderByIdCmd = new FindOrderByIdCmd();
        findBuyOrderByIdCmd.setOrderId(buyOrderId);
        FindOrderByIdResp findBuyOrderByIdAfterMatchResp = commandService.execute(findBuyOrderByIdCmd).join();

        FindOrderByIdCmd findSellOrderByIdCmd = new FindOrderByIdCmd();
        findSellOrderByIdCmd.setOrderId(sellOrderId);
        FindOrderByIdResp findSellOrderByIdAfterMatchResp = commandService.execute(findSellOrderByIdCmd).join();

        Assert.assertEquals(findBuyOrderByIdAfterMatchResp.getOrder().getRemainedQuantity(), 0);
        Assert.assertEquals(findBuyOrderByIdAfterMatchResp.getOrder().getOrderState(), OrderState.TradedCompletely);

        Assert.assertEquals(findSellOrderByIdAfterMatchResp.getOrder().getRemainedQuantity(), 10);
        Assert.assertEquals(findSellOrderByIdAfterMatchResp.getOrder().getOrderState(), OrderState.PartiallyTraded);


    }

    @Test
    public void OrdersWithDifferentStockMatchTest() {
        ICommandService commandService = getCommandService();

        String firstStockId = addStock(300.5);
        String secondStockId = addStock(300.5);
        String firstUserId = addUser();
        String secondUserId = addUser();

        ExecutionOptions executionOptions = new ExecutionOptions();


        String buyOrderId = addOrder(firstUserId, firstStockId, 20, 300.6, OrderType.Buy, executionOptions);


        addPortfolio(secondUserId, secondStockId, 30);
        String sellOrderId = addOrder(secondUserId, secondStockId, 20, 300.6, OrderType.Sell, executionOptions);


        waitForAllTasks();

        FindOrderByIdCmd findBuyOrderByIdCmd = new FindOrderByIdCmd();
        findBuyOrderByIdCmd.setOrderId(buyOrderId);
        FindOrderByIdResp findBuyOrderByIdAfterMatchResp = commandService.execute(findBuyOrderByIdCmd).join();

        FindOrderByIdCmd findSellOrderByIdCmd = new FindOrderByIdCmd();
        findSellOrderByIdCmd.setOrderId(sellOrderId);
        FindOrderByIdResp findSellOrderByIdAfterMatchResp = commandService.execute(findSellOrderByIdCmd).join();


        Assert.assertEquals(findBuyOrderByIdAfterMatchResp.getOrder().getOrderState(), OrderState.PendingTrade);
        Assert.assertEquals(findSellOrderByIdAfterMatchResp.getOrder().getOrderState(), OrderState.PendingTrade);


    }

    @Test
    public void StockPriceUpdateAfterMatchTest() {

        ICommandService commandService = getCommandService();

        String stockId = addStock(300.5);
        String firstUserId = addUser();
        String secondUserId = addUser();

        ExecutionOptions executionOptions = new ExecutionOptions();

        String buyOrderId = addOrder(firstUserId, stockId, 20, 300.6, OrderType.Buy, executionOptions);

        addPortfolio(secondUserId, stockId, 20);
        String sellOrderId = addOrder(secondUserId, stockId, 20, 300.6, OrderType.Sell, executionOptions);


        waitForAllTasks();

        FindOrderByIdCmd findBuyOrderByIdCmd = new FindOrderByIdCmd();
        findBuyOrderByIdCmd.setOrderId(buyOrderId);
        FindOrderByIdResp findBuyOrderByIdAfterMatchResp = commandService.execute(findBuyOrderByIdCmd).join();

        FindOrderByIdCmd findSellOrderByIdCmd = new FindOrderByIdCmd();
        findSellOrderByIdCmd.setOrderId(sellOrderId);
        FindOrderByIdResp findSellOrderByIdAfterMatchResp = commandService.execute(findSellOrderByIdCmd).join();

        FindStockByIdCmd findStockByIdCmd = new FindStockByIdCmd();
        findStockByIdCmd.setId(stockId);
        FindStockByIdResp findStockByIdResp = commandService.execute(findStockByIdCmd).join();


        Assert.assertEquals(findBuyOrderByIdAfterMatchResp.getOrder().getOrderState(), OrderState.TradedCompletely);
        Assert.assertEquals(findBuyOrderByIdAfterMatchResp.getOrder().getRemainedQuantity(), 0);

        Assert.assertEquals(findSellOrderByIdAfterMatchResp.getOrder().getOrderState(), OrderState.TradedCompletely);
        Assert.assertEquals(findSellOrderByIdAfterMatchResp.getOrder().getRemainedQuantity(), 0);


        Assert.assertEquals(findStockByIdResp.getStock().getPrice(), 300.6, 0);


    }

    @Test
    public void StockPortfolioUpdateAfterMatchTest() {

        ICommandService commandService = getCommandService();

        String stockId = addStock(300.5);
        String firstUserId = addUser();
        String secondUserId = addUser();

        ExecutionOptions executionOptions = new ExecutionOptions();


        GetUserStockPortfolioCmd getUserStockPortfolioCmd = new GetUserStockPortfolioCmd();
        getUserStockPortfolioCmd.setStockId(stockId);
        getUserStockPortfolioCmd.setUserId(firstUserId);

        GetUserStockPortfolioResp getUserStockPortfolioResp = commandService.execute(getUserStockPortfolioCmd).join();

        Assert.assertEquals(getUserStockPortfolioResp.getStockPortfolio().getQuantity(), 0);
        Assert.assertEquals(getUserStockPortfolioResp.getStockPortfolio().getUserId(), firstUserId);

        String buyOrderId = addOrder(firstUserId, stockId, 20, 300.6, OrderType.Buy, executionOptions);

        addPortfolio(secondUserId, stockId, 20);
        String sellOrderId = addOrder(secondUserId, stockId, 20, 300.6, OrderType.Sell, executionOptions);


        waitForAllTasks();

        GetUserStockPortfolioResp getUserStockPortfolioAfterMatchResp =
                commandService.execute(getUserStockPortfolioCmd).join();

        Assert.assertEquals(getUserStockPortfolioAfterMatchResp.getStockPortfolio().getQuantity(), 20);
        Assert.assertEquals(getUserStockPortfolioAfterMatchResp.getStockPortfolio().getUserId(), firstUserId);


    }

    @Test
    public void withdrawOrderTest() {

        ICommandService commandService = getCommandService();

        String stockId = addStock(300.5);
        String firstUserId = addUser();


        ExecutionOptions executionOptions = new ExecutionOptions();
        executionOptions.addExecutionParameter(OrderExeutionParameters.DISABLE_ORDER_MATCH, true);

        String buyOrderId = addOrder(firstUserId, stockId, 20, 300.6, OrderType.Buy, executionOptions);

        WithdrawOrderCmd withdrawOrderCmd = new WithdrawOrderCmd();
        withdrawOrderCmd.setOrderId(buyOrderId);

        commandService.execute(withdrawOrderCmd).join();

        FindOrderByIdCmd findBuyOrderByIdCmd = new FindOrderByIdCmd();
        findBuyOrderByIdCmd.setOrderId(buyOrderId);
        FindOrderByIdResp findBuyOrderByIdAfterMatchResp = commandService.execute(findBuyOrderByIdCmd).join();


        Assert.assertEquals(findBuyOrderByIdAfterMatchResp.getOrder().getOrderState(), OrderState.Canceled);
        Assert.assertEquals(findBuyOrderByIdAfterMatchResp.getOrder().getRemainedQuantity(), 20);


    }

    @Test
    public void partialWithdrawOrderTest() {

        ICommandService commandService = getCommandService();

        String stockId = addStock(300.5);
        String firstUserId = addUser();
        String secondUserId = addUser();

        ExecutionOptions executionOptions = new ExecutionOptions();


        String buyOrderId = addOrder(firstUserId, stockId, 20, 300.6, OrderType.Buy, executionOptions);

        addPortfolio(secondUserId, stockId, 10);
        String sellOrderId = addOrder(secondUserId, stockId, 10, 300.6, OrderType.Sell, executionOptions);

        waitForAllTasks();

        WithdrawOrderCmd withdrawOrderCmd = new WithdrawOrderCmd();
        withdrawOrderCmd.setOrderId(buyOrderId);

        commandService.execute(withdrawOrderCmd).join();


        FindOrderByIdCmd findBuyOrderByIdCmd = new FindOrderByIdCmd();
        findBuyOrderByIdCmd.setOrderId(buyOrderId);
        FindOrderByIdResp findBuyOrderByIdAfterMatchResp = commandService.execute(findBuyOrderByIdCmd).join();


        FindOrderByIdCmd findSellOrderByIdCmd = new FindOrderByIdCmd();
        findSellOrderByIdCmd.setOrderId(sellOrderId);
        FindOrderByIdResp findSellOrderByIdAfterMatchResp = commandService.execute(findSellOrderByIdCmd).join();


        Assert.assertEquals(findBuyOrderByIdAfterMatchResp.getOrder().getOrderState(), OrderState.PartiallyCanceled);
        Assert.assertEquals(findBuyOrderByIdAfterMatchResp.getOrder().getRemainedQuantity(), 10);

        Assert.assertEquals(findSellOrderByIdAfterMatchResp.getOrder().getOrderState(), OrderState.TradedCompletely);
        Assert.assertEquals(findSellOrderByIdAfterMatchResp.getOrder().getRemainedQuantity(), 0);


    }


    @Test(expected = CommandExecutionException.class)
    public void withdrawAlreadyWithdrawnOrderTest() {

        ICommandService commandService = getCommandService();

        String stockId = addStock(300.5);
        String firstUserId = addUser();
        String secondUserId = addUser();

        ExecutionOptions executionOptions = new ExecutionOptions();


        String buyOrderId = addOrder(firstUserId, stockId, 20, 300.6, OrderType.Buy, executionOptions);

        waitForAllTasks();

        WithdrawOrderCmd withdrawOrderCmd = new WithdrawOrderCmd();
        withdrawOrderCmd.setOrderId(buyOrderId);
        commandService.execute(withdrawOrderCmd).join();


        FindOrderByIdCmd findBuyOrderByIdCmd = new FindOrderByIdCmd();
        findBuyOrderByIdCmd.setOrderId(buyOrderId);
        FindOrderByIdResp findBuyOrderByIdAfterMatchResp = commandService.execute(findBuyOrderByIdCmd).join();


        Assert.assertEquals(findBuyOrderByIdAfterMatchResp.getOrder().getOrderState(), OrderState.Canceled);
        Assert.assertEquals(findBuyOrderByIdAfterMatchResp.getOrder().getRemainedQuantity(), 20);

        //withdraw again
        commandService.execute(withdrawOrderCmd).join();


    }

    @Test
    public void withdrawOrderAndPortfolioIntegrationTest() {

        ICommandService commandService = getSystemCommandService();

        String stockId = addStock(300.5);
        String firstUserId = addUser();
        String secondUserId = addUser();

        ExecutionOptions executionOptions = new ExecutionOptions();


        String buyOrderId = addOrder(firstUserId, stockId, 25, 300.6, OrderType.Buy, executionOptions);

        addPortfolio(secondUserId, stockId, 10);
        String sellOrderId = addOrder(secondUserId, stockId, 10, 300.6, OrderType.Sell, executionOptions);

        waitForAllTasks();

        WithdrawOrderCmd withdrawOrderCmd = new WithdrawOrderCmd();
        withdrawOrderCmd.setOrderId(buyOrderId);

        commandService.execute(withdrawOrderCmd).join();


        FindOrderByIdCmd findBuyOrderByIdCmd = new FindOrderByIdCmd();
        findBuyOrderByIdCmd.setOrderId(buyOrderId);
        FindOrderByIdResp findBuyOrderByIdAfterMatchResp = commandService.execute(findBuyOrderByIdCmd).join();


        FindOrderByIdCmd findSellOrderByIdCmd = new FindOrderByIdCmd();
        findSellOrderByIdCmd.setOrderId(sellOrderId);
        FindOrderByIdResp findSellOrderByIdAfterMatchResp = commandService.execute(findSellOrderByIdCmd).join();


        Assert.assertEquals(findBuyOrderByIdAfterMatchResp.getOrder().getOrderState(), OrderState.PartiallyCanceled);
        Assert.assertEquals(findBuyOrderByIdAfterMatchResp.getOrder().getRemainedQuantity(), 15);

        Assert.assertEquals(findSellOrderByIdAfterMatchResp.getOrder().getOrderState(), OrderState.TradedCompletely);
        Assert.assertEquals(findSellOrderByIdAfterMatchResp.getOrder().getRemainedQuantity(), 0);

        GetUserStockPortfolioCmd getUserStockPortfolioCmd = new GetUserStockPortfolioCmd();
        getUserStockPortfolioCmd.setUserId(firstUserId);
        getUserStockPortfolioCmd.setStockId(stockId);
        GetUserStockPortfolioResp getUserStockPortfolioResp = commandService.execute(getUserStockPortfolioCmd).join();

        Assert.assertEquals(getUserStockPortfolioResp.getStockPortfolio().getUserId(), firstUserId);
        Assert.assertEquals(getUserStockPortfolioResp.getStockPortfolio().getStockId(), stockId);
        Assert.assertEquals(getUserStockPortfolioResp.getStockPortfolio().getQuantity(), 10);


    }

}

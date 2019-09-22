package edu.rmit.sef.stocktradingserver.order;

import edu.rmit.command.core.ExecutionOptions;
import edu.rmit.command.core.ICommandService;
import edu.rmit.command.core.ICommandStore;
import edu.rmit.sef.order.command.CreateOrderCmd;
import edu.rmit.sef.order.command.FindOrderByIdCmd;
import edu.rmit.sef.order.command.FindOrderByIdResp;
import edu.rmit.sef.order.model.OrderState;
import edu.rmit.sef.order.model.OrderType;
import edu.rmit.sef.portfolio.command.GetUserStockPortfolioCmd;
import edu.rmit.sef.portfolio.command.GetUserStockPortfolioResp;
import edu.rmit.sef.stock.command.FindStockByIdCmd;
import edu.rmit.sef.stock.command.FindStockByIdResp;
import edu.rmit.sef.stocktradingserver.core.BaseTest;
import edu.rmit.sef.stocktradingserver.order.command.MatchOrderCmd;
import edu.rmit.sef.stocktradingserver.order.repo.OrderRepository;
import net.bytebuddy.implementation.bind.annotation.Super;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.CompletionException;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class OrderMatchTests extends BaseTest {


    @Test
    public void OrderMatchTest() {
        ICommandService commandService = getCommandService();

        String stockId = addStock(300.5);
        String firstUserId = addUser();
        String secondUserId = addUser();

        ExecutionOptions executionOptions = new ExecutionOptions();
        executionOptions.setIgnoreAsyncHandlers(true);

        String buyOrderId = addOrder(firstUserId, stockId, 20, 300.6, OrderType.Buy, executionOptions);

        FindOrderByIdCmd findBuyOrderByIdCmd = new FindOrderByIdCmd();
        findBuyOrderByIdCmd.setOrderId(buyOrderId);
        FindOrderByIdResp findBuyOrderByIdResp = commandService.execute(findBuyOrderByIdCmd).join();

        addPortfolio(secondUserId, stockId, 20);

        Assert.assertEquals(findBuyOrderByIdResp.getOrder().getOrderState(), OrderState.PendingTrade);

        String sellOrderId = addOrder(secondUserId, stockId, 20, 300.6, OrderType.Sell, executionOptions);

        FindOrderByIdCmd findSellOrderByIdCmd = new FindOrderByIdCmd();
        findSellOrderByIdCmd.setOrderId(sellOrderId);
        FindOrderByIdResp findSellOrderByIdResp = commandService.execute(findSellOrderByIdCmd).join();

        Assert.assertEquals(findSellOrderByIdResp.getOrder().getOrderState(), OrderState.PendingTrade);


        MatchOrderCmd matchBuyOrderCmd = new MatchOrderCmd();
        matchBuyOrderCmd.setOrderId(buyOrderId);
        commandService.execute(matchBuyOrderCmd).join();

        waitForAsyncTasks();


        FindOrderByIdResp findBuyOrderByIdAfterMatchResp = commandService.execute(findBuyOrderByIdCmd).join();
        FindOrderByIdResp findSellOrderByIdAfterMatchResp = commandService.execute(findSellOrderByIdCmd).join();


        Assert.assertEquals(findBuyOrderByIdAfterMatchResp.getOrder().getOrderState(), OrderState.TradedCompletely);
        Assert.assertEquals(findSellOrderByIdAfterMatchResp.getOrder().getOrderState(), OrderState.TradedCompletely);


    }

    @Test(expected = CompletionException.class)
    public void OrderStateMatchTest() {
        ICommandService commandService = getCommandService();

        String stockId = addStock(300.5);
        String firstUserId = addUser();
        String secondUserId = addUser();

        ExecutionOptions executionOptions = new ExecutionOptions();
        executionOptions.setIgnoreAsyncHandlers(true);


        String buyOrderId = addOrder(firstUserId, stockId, 20, 300.6, OrderType.Buy, executionOptions);

        String sellOrderId = addOrder(secondUserId, stockId, 20, 300.6, OrderType.Sell, executionOptions);


        MatchOrderCmd matchBuyOrderCmd = new MatchOrderCmd();
        matchBuyOrderCmd.setOrderId(buyOrderId);
        commandService.execute(matchBuyOrderCmd).join();

        waitForAsyncTasks();

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
        commandService.execute(matchBuyOrderCmd).join();

        waitForAsyncTasks();


    }

    @Test
    public void PartialOrderMatchTest() {
        ICommandService commandService = getCommandService();

        String stockId = addStock(300.5);
        String firstUserId = addUser();
        String secondUserId = addUser();

        ExecutionOptions executionOptions = new ExecutionOptions();

        String buyOrderId = addOrder(firstUserId, stockId, 20, 300.6, OrderType.Buy, executionOptions);

        String sellOrderId = addOrder(secondUserId, stockId, 30, 300.6, OrderType.Sell, executionOptions);


        waitForAsyncTasks();

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
    public void DifferentStockMatchTest() {
        ICommandService commandService = getCommandService();

        String firstStockId = addStock(300.5);
        String secondStockId = addStock(300.5);
        String firstUserId = addUser();
        String secondUserId = addUser();

        ExecutionOptions executionOptions = new ExecutionOptions();

        String buyOrderId = addOrder(firstUserId, firstStockId, 20, 300.6, OrderType.Buy, executionOptions);

        String sellOrderId = addOrder(secondUserId, secondStockId, 20, 300.6, OrderType.Sell, executionOptions);


        waitForAsyncTasks();

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
        getUserStockPortfolioCmd.setUserId(secondUserId);

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


}

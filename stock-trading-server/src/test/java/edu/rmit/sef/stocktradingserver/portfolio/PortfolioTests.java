package edu.rmit.sef.stocktradingserver.portfolio;


import edu.rmit.command.core.ICommandService;
import edu.rmit.sef.portfolio.command.GetUserStockPortfolioCmd;
import edu.rmit.sef.portfolio.command.GetUserStockPortfolioResp;
import edu.rmit.sef.portfolio.model.StockPortfolio;
import edu.rmit.sef.stocktradingserver.core.BaseTest;
import edu.rmit.sef.stocktradingserver.portfolio.command.UpdateUserStockPortfolioCmd;
import edu.rmit.sef.user.command.AuthenticateCmd;
import edu.rmit.sef.user.command.AuthenticateResp;
import edu.rmit.sef.user.command.RegisterUserCmd;
import edu.rmit.sef.user.command.RegisterUserResp;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.CompletionException;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class PortfolioTests extends BaseTest {



    @Test
    public void createPortfolioTest() {

        ICommandService commandService = getCommandService();

        String userId = addUser();
        String stockId = addStock();

        GetUserStockPortfolioCmd getUserStockPortfolioCmd = new GetUserStockPortfolioCmd();
        getUserStockPortfolioCmd.setUserId(userId);
        getUserStockPortfolioCmd.setStockId(stockId);


        GetUserStockPortfolioResp getUserStockPortfolioResp = commandService.execute(getUserStockPortfolioCmd).join();

        StockPortfolio userPortfolio = getUserStockPortfolioResp.getStockPortfolio();

        Assert.assertEquals(userPortfolio.getQuantity(), 0);
        Assert.assertEquals(userPortfolio.getStockId(), stockId);
        Assert.assertEquals(userPortfolio.getUserId(), userId);


    }

    @Test
    public void updateStockPortfolioTest() {

        ICommandService commandService = getCommandService();

        String userId = addUser();
        String stockId = addStock();


        UpdateUserStockPortfolioCmd updateUserStockPortfolioCmd = new UpdateUserStockPortfolioCmd();
        updateUserStockPortfolioCmd.setUserId(userId);
        updateUserStockPortfolioCmd.setStockId(stockId);
        updateUserStockPortfolioCmd.setQuantityChanged(100);

        commandService.execute(updateUserStockPortfolioCmd).join();

        GetUserStockPortfolioCmd getUserStockPortfolioCmd = new GetUserStockPortfolioCmd();
        getUserStockPortfolioCmd.setUserId(userId);
        getUserStockPortfolioCmd.setStockId(stockId);


        GetUserStockPortfolioResp getUserStockPortfolioResp = commandService.execute(getUserStockPortfolioCmd).join();

        StockPortfolio userPortfolio = getUserStockPortfolioResp.getStockPortfolio();

        Assert.assertEquals(userPortfolio.getQuantity(), 100);
        Assert.assertEquals(userPortfolio.getStockId(), stockId);
        Assert.assertEquals(userPortfolio.getUserId(), userId);


    }

    @Test(expected = CompletionException.class)
    public void clientShareGuardTest() {

        ICommandService commandService = getCommandService();

        String userId = addUser();
        String stockId = addStock();


        UpdateUserStockPortfolioCmd updateUserStockPortfolioCmd = new UpdateUserStockPortfolioCmd();
        updateUserStockPortfolioCmd.setUserId(userId);
        updateUserStockPortfolioCmd.setStockId(stockId);
        updateUserStockPortfolioCmd.setQuantityChanged(100);

        commandService.execute(updateUserStockPortfolioCmd).join();


        UpdateUserStockPortfolioCmd againUpdateUserStockPortfolioCmd = new UpdateUserStockPortfolioCmd();
        againUpdateUserStockPortfolioCmd.setUserId(userId);
        againUpdateUserStockPortfolioCmd.setStockId(stockId);
        againUpdateUserStockPortfolioCmd.setQuantityChanged(-200);


        commandService.execute(againUpdateUserStockPortfolioCmd).join();

    }

    @Test(expected = CompletionException.class)
    public void  zeroQuantityCheck() {

        ICommandService commandService = getCommandService();

        String userId = addUser();
        String stockId = addStock();


        UpdateUserStockPortfolioCmd updateUserStockPortfolioCmd = new UpdateUserStockPortfolioCmd();
        updateUserStockPortfolioCmd.setUserId(userId);
        updateUserStockPortfolioCmd.setStockId(stockId);
        updateUserStockPortfolioCmd.setQuantityChanged(0);

        commandService.execute(updateUserStockPortfolioCmd).join();


    }

}

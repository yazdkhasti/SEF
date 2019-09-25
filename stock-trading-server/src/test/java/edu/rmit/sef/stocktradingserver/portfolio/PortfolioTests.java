package edu.rmit.sef.stocktradingserver.portfolio;


import edu.rmit.command.core.ICommandService;
import edu.rmit.command.exception.CommandExecutionException;
import edu.rmit.sef.core.command.CreateEntityResp;
import edu.rmit.sef.core.security.Authority;
import edu.rmit.sef.portfolio.command.GetUserStockPortfolioCmd;
import edu.rmit.sef.portfolio.command.GetUserStockPortfolioResp;
import edu.rmit.sef.portfolio.model.StockPortfolio;
import edu.rmit.sef.stocktradingserver.core.BaseTest;
import edu.rmit.sef.stocktradingserver.portfolio.command.UpdateUserStockPortfolioCmd;
import edu.rmit.sef.user.command.RegisterUserCmd;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

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

        ICommandService commandService = getSystemCommandService();

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

    @Test(expected = CommandExecutionException.class)
    public void clientShareGuardTest() {

        ICommandService commandService = getSystemCommandService();

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

    @Test(expected = CommandExecutionException.class)
    public void zeroQuantityCheck() {

        ICommandService commandService = getSystemCommandService();

        String userId = addUser();
        String stockId = addStock();


        UpdateUserStockPortfolioCmd updateUserStockPortfolioCmd = new UpdateUserStockPortfolioCmd();
        updateUserStockPortfolioCmd.setUserId(userId);
        updateUserStockPortfolioCmd.setStockId(stockId);
        updateUserStockPortfolioCmd.setQuantityChanged(0);

        commandService.execute(updateUserStockPortfolioCmd).join();


    }

    @Test(expected = SecurityException.class)
    public void adminGuardNegativeTest() {

        ICommandService commandService = getCommandService();

        String userId = addUser();
        String stockId = addStock();


        UpdateUserStockPortfolioCmd updateUserStockPortfolioCmd = new UpdateUserStockPortfolioCmd();
        updateUserStockPortfolioCmd.setUserId(userId);
        updateUserStockPortfolioCmd.setStockId(stockId);
        updateUserStockPortfolioCmd.setQuantityChanged(100);

        commandService.execute(updateUserStockPortfolioCmd).join();


    }

    @Test()
    public void adminGuardTest() {

        ICommandService commandService = getSystemCommandService();

        RegisterUserCmd registerUserCmd = new RegisterUserCmd();
        registerUserCmd.setFirstName("Carol");
        registerUserCmd.setLastName("liu");
        registerUserCmd.setUsername("carollllll");
        registerUserCmd.setCompany("rmit");
        registerUserCmd.setPassword("pwd");

        List<String> authorities = new ArrayList<>();
        authorities.add(Authority.ADMIN);
        registerUserCmd.setAuthorities(authorities);

        CreateEntityResp registerUserResp = commandService.execute(registerUserCmd).join();

        String userId = addUser();
        String stockId = addStock();

        ICommandService adminCommandService = getCommandService(registerUserResp.getId());

        UpdateUserStockPortfolioCmd updateUserStockPortfolioCmd = new UpdateUserStockPortfolioCmd();
        updateUserStockPortfolioCmd.setUserId(userId);
        updateUserStockPortfolioCmd.setStockId(stockId);
        updateUserStockPortfolioCmd.setQuantityChanged(100);

        adminCommandService.execute(updateUserStockPortfolioCmd).join();


    }

}

package edu.rmit.sef.stocktradingserver.core;

import edu.rmit.command.core.ICommandService;
import edu.rmit.command.core.ICommandServiceFactory;
import edu.rmit.sef.core.command.CreateEntityResp;
import edu.rmit.sef.order.command.CreateOrderCmd;
import edu.rmit.sef.order.model.OrderType;
import edu.rmit.sef.stock.command.AddStockCmd;
import edu.rmit.sef.stocktradingserver.portfolio.command.UpdateUserStockPortfolioCmd;
import edu.rmit.sef.user.command.RegisterUserCmd;
import edu.rmit.sef.user.command.RegisterUserResp;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Random;
import java.util.UUID;


public class BaseTest {
    @Autowired
    private ICommandServiceFactory commandServiceFactory;

    public ICommandService getCommandService() {
        return commandServiceFactory.createService();
    }

    public ICommandService getCommandService(String userId) {
        return commandServiceFactory.createService(userId);
    }

    public String addUser() {

        ICommandService commandService = getCommandService();

        Random random = new Random();
        String userId = String.valueOf(Math.abs(random.nextInt()));
        String userName = userId + "-user";

        RegisterUserCmd registerUserCmd = new RegisterUserCmd();
        registerUserCmd.setFirstName("TestF");
        registerUserCmd.setLastName(userName);
        registerUserCmd.setUsername(userName);
        registerUserCmd.setCompany("rmit");
        registerUserCmd.setPassword("pwd");

        RegisterUserResp registerUserResp = commandService.execute(registerUserCmd).join();

        return registerUserResp.getId();

    }

    public String addStock() {
        return addStock(100.00);
    }

    public String addStock(double price) {

        ICommandService commandService = getCommandService();

        AddStockCmd addStockCmd = new AddStockCmd();
        addStockCmd.setSymbol("goog");
        addStockCmd.setName("Google");
        addStockCmd.setPrice(100);

        CreateEntityResp createEntityResp = commandService.execute(addStockCmd).join();

        return createEntityResp.getId();

    }

    public void addPortfolio(String userId, String stockId, int quantity) {

        ICommandService commandService = getCommandService();

        UpdateUserStockPortfolioCmd updateUserStockPortfolioCmd = new UpdateUserStockPortfolioCmd();
        updateUserStockPortfolioCmd.setStockId(stockId);
        updateUserStockPortfolioCmd.setQuantityChanged(quantity);
        updateUserStockPortfolioCmd.setUserId(userId);

        commandService.execute(updateUserStockPortfolioCmd).join();


    }

    public String addOrder(String userId, String stockId, int quantity, double price, OrderType orderType) {

        ICommandService commandService = getCommandService(userId);

        CreateOrderCmd createOrderCmd = new CreateOrderCmd();
        createOrderCmd.setOrderType(orderType);
        createOrderCmd.setPrice(price);
        createOrderCmd.setQuantity(quantity);
        createOrderCmd.setStockId(stockId);


        CreateEntityResp createEntityResp = commandService.execute(createOrderCmd).join();

        return createEntityResp.getId();

    }


}

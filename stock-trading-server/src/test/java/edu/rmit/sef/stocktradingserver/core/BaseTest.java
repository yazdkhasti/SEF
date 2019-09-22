package edu.rmit.sef.stocktradingserver.core;

import com.sun.org.apache.xml.internal.resolver.readers.ExtendedXMLCatalogReader;
import edu.rmit.command.core.ExecutionOptions;
import edu.rmit.command.core.ICommandService;
import edu.rmit.command.core.ICommandServiceFactory;
import edu.rmit.command.core.ICommandStore;
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

    @Autowired
    private ICommandStore commandStore;

    private ICommandService commandService;

    public ICommandService getCommandService() {
        return this.commandService == null
                ? commandServiceFactory.createService()
                : this.commandService;
    }

    public ICommandService getCommandService(String userId) {
        return commandServiceFactory.createService(userId);
    }

    private void setCommandService(ICommandService commandService) {
        this.commandService = commandService;
    }

    public void sleep(long millSeconds) {
        try {
            Thread.sleep(millSeconds);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public void waitForAsyncTasks() {
        while (commandStore.getAsyncTaskCount() > 0) {
            sleep(100);
        }
    }

    public void waitForAllTasks() {
        while (commandStore.getTasKCount() > 0 || commandStore.getAsyncTaskCount() > 0) {
            sleep(100);
        }
    }

    private String getRandomString(String prefix) {
        Random random = new Random();
        String randomValue = String.valueOf(Math.abs(random.nextInt()));
        String result = prefix + "-" + randomValue;
        return result;
    }

    public String addUser() {

        ICommandService commandService = getCommandService();

        String username = getRandomString("user");

        RegisterUserCmd registerUserCmd = new RegisterUserCmd();
        registerUserCmd.setFirstName("TestF");
        registerUserCmd.setLastName(username);
        registerUserCmd.setUsername(username);
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

        String symbol = getRandomString("symbol");

        AddStockCmd addStockCmd = new AddStockCmd();
        addStockCmd.setSymbol(symbol);
        addStockCmd.setName(symbol);
        addStockCmd.setPrice(price);

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

    public String addOrder(String userId, String stockId, int quantity, double price, OrderType orderType, ExecutionOptions options) {

        ICommandService commandService = getCommandService(userId);

        CreateOrderCmd createOrderCmd = new CreateOrderCmd();
        createOrderCmd.setOrderType(orderType);
        createOrderCmd.setPrice(price);
        createOrderCmd.setQuantity(quantity);
        createOrderCmd.setStockId(stockId);


        CreateEntityResp createEntityResp = commandService.execute(createOrderCmd, options).join();

        return createEntityResp.getId();

    }


}

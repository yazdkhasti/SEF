package edu.rmit.sef.stocktradingserver.core;

import edu.rmit.command.core.ICommandService;
import edu.rmit.command.core.ICommandServiceFactory;
import edu.rmit.sef.core.command.CreateEntityResp;
import edu.rmit.sef.stock.command.AddStockCmd;
import edu.rmit.sef.user.command.RegisterUserCmd;
import edu.rmit.sef.user.command.RegisterUserResp;
import org.springframework.beans.factory.annotation.Autowired;


public class BaseTest {
    @Autowired
    private ICommandServiceFactory commandServiceFactory;

    public ICommandService getCommandService() {
        return commandServiceFactory.createService();
    }

    public String addUser() {

        ICommandService commandService = getCommandService();

        RegisterUserCmd registerUserCmd = new RegisterUserCmd();
        registerUserCmd.setFirstName("TestF");
        registerUserCmd.setLastName("TestL");
        registerUserCmd.setUsername("test");
        registerUserCmd.setCompany("rmit");
        registerUserCmd.setPassword("pwd");

        RegisterUserResp registerUserResp = commandService.execute(registerUserCmd).join();

        return registerUserResp.getId();

    }

    public String addStock() {

        ICommandService commandService = getCommandService();

        AddStockCmd addStockCmd = new AddStockCmd();
        addStockCmd.setSymbol("goog");
        addStockCmd.setName("Google");
        addStockCmd.setPrice(100);

        CreateEntityResp createEntityResp = commandService.execute(addStockCmd).join();

        return createEntityResp.getId();

    }

}

package edu.rmit.sef.stocktradingserver.core;

import edu.rmit.command.core.ICommandService;
import edu.rmit.command.core.ICommandServiceFactory;
import org.springframework.beans.factory.annotation.Autowired;



public class BaseTest {
    @Autowired
    private ICommandService commandService;

    public ICommandService getCommandService() {
        return commandService;
    }

}

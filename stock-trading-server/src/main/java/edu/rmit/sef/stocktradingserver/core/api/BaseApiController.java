package edu.rmit.sef.stocktradingserver.core.api;


import edu.rmit.command.core.ICommandService;
import edu.rmit.command.core.ICommandServiceFactory;
import org.springframework.beans.factory.annotation.Autowired;


public abstract class BaseApiController {

    @Autowired
    private ICommandServiceFactory commandServiceFactory;


    protected ICommandService getCommandService() {
        return commandServiceFactory.createService(null);
    }

    public BaseApiController() {

    }

}

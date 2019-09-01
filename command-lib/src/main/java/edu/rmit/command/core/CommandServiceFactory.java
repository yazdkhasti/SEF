package edu.rmit.command.core;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CommandServiceFactory implements ICommandServiceFactory {


    @Autowired
    private ICommandStore store;

    @Autowired
    private IExecutionContextFactory executionContextFactory;

    @Override
    public ICommandService createService() {
        return new CommandService(null, null, store, executionContextFactory);
    }

    @Override
    public ICommandService createService(String userId) {
        return new CommandService(userId, null, store, executionContextFactory);
    }

    @Override
    public ICommandService createService(String userId, IExecutionContext parentContext) {
        return new CommandService(userId, parentContext, store, executionContextFactory);
    }

}

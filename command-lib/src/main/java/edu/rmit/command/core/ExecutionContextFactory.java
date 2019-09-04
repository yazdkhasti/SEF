package edu.rmit.command.core;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class ExecutionContextFactory implements IExecutionContextFactory {

    @Autowired
    private IServiceResolver serviceResolver;

    @Autowired
    private ICommandServiceFactory commandServiceFactory;

    @Override
    public <T extends ICommand> ICommandExecutionContext<T> create(T command, String userId) {
        return create(command, userId, null);
    }

    @Override
    public <T extends ICommand> ICommandExecutionContext<T> create(T command, String userId, IExecutionContext parent) {
        return new ExecutionContext<>(command, userId, serviceResolver, commandServiceFactory, parent);
    }
}

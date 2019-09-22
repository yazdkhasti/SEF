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
    public <T extends ICommand> ICommandExecutionContext<T> create(T command, String userId, ExecutionOptions options) {
        return create(command, userId, options, null);
    }

    @Override
    public <T extends ICommand> ICommandExecutionContext<T> create(T command, String userId, ExecutionOptions options, IExecutionContext parent) {
        return new ExecutionContext(command, userId, serviceResolver, commandServiceFactory, options, parent);
    }


}

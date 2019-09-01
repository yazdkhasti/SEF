package edu.rmit.command.core;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class ExecutionContextFactory implements IExecutionContextFactory {

    @Autowired
    private IServiceResolver serviceResolver;


    @Override
    public <T extends ICommand> ICommandExecutionContext<T> create(T command, String userId) {
        return new ExecutionContext<>(command, userId, serviceResolver, null);
    }

    @Override
    public <T extends ICommand> ICommandExecutionContext<T> create(T command, String userId, IExecutionContext parent) {
        return new ExecutionContext<>(command, userId, serviceResolver, parent);
    }
}

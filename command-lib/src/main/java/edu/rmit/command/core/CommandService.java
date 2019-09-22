package edu.rmit.command.core;

import java.util.concurrent.CompletableFuture;


public class CommandService implements ICommandService {

    private String userId;
    private IExecutionContext parentContext;
    private ICommandStore store;
    private IExecutionContextFactory executionContextFactory;


    public CommandService(String userId, IExecutionContext parentContext, ICommandStore store, IExecutionContextFactory executionContextFactory) {
        this.userId = userId;
        this.parentContext = parentContext;
        this.store = store;
        this.executionContextFactory = executionContextFactory;
    }


    @Override
    public String getUserId() {
        return null;
    }

    @Override
    public <R, T extends ICommand<R>> CompletableFuture<R> execute(T command) {
        ExecutionOptions options;
        if (parentContext != null) {
            options = parentContext.getOptions();
        } else {
            options = new ExecutionOptions();
        }
        return execute(command, options);
    }

    @Override
    public <R, T extends ICommand<R>> CompletableFuture<R> execute(T command, ExecutionOptions options) {
        ICommandExecutionContext<T> context = executionContextFactory.create(command, userId, options, parentContext);
        return store.execute(context, options);
    }
}

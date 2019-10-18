package edu.rmit.command.core;


import java.util.concurrent.CompletableFuture;

public interface ICommandStore {
    int getAsyncTaskCount();

    int getTasKCount();

    <R, T extends ICommand<R>> CompletableFuture<R> execute(ICommandExecutionContext<T> context, ExecutionOptions options);
}

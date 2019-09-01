package edu.rmit.command.core;


import java.util.concurrent.CompletableFuture;

public interface ICommandStore {
    <R, T extends ICommand<R>> CompletableFuture<R> execute(ICommandExecutionContext<T> context);
}

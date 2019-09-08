package edu.rmit.command.core;

import org.springframework.util.concurrent.ListenableFuture;

public interface ICommandPostHandler<T extends ICommand> {
    void handle(ICommandExecutionContext<T> executionContext) ;
}

package edu.rmit.command.core;

import org.springframework.util.concurrent.ListenableFuture;

public interface ICommandHandlerAsync<T extends ICommand> {
    ListenableFuture handle(T command);
}

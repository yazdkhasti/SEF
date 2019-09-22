package edu.rmit.command.core;

public interface IAsyncCommandHandler<T extends ICommand> {
    void handle(ICommandExecutionContext<T> executionContext);
}

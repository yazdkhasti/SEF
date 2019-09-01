package edu.rmit.command.core;

public interface ICommandHandler<T extends ICommand> {
    void handle(ICommandExecutionContext<T> executionContext) ;
}

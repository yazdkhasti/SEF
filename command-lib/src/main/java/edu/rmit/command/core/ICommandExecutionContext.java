package edu.rmit.command.core;

public interface ICommandExecutionContext<T extends ICommand> extends IExecutionContext {

    T getCommand();

}

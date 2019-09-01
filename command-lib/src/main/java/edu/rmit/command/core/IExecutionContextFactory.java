package edu.rmit.command.core;

public interface IExecutionContextFactory {
    <T extends ICommand> ICommandExecutionContext<T> create(T command, String userId);

    <T extends ICommand> ICommandExecutionContext<T> create(T command, String userId, IExecutionContext parent);
}

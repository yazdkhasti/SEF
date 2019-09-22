package edu.rmit.command.core;

public interface IExecutionContextFactory {
    <T extends ICommand> ICommandExecutionContext<T> create(T command, String userId, ExecutionOptions options);

    <T extends ICommand> ICommandExecutionContext<T> create(T command, String userId, ExecutionOptions options, IExecutionContext parent);
}

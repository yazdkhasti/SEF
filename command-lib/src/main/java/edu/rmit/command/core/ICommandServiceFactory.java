package edu.rmit.command.core;

public interface ICommandServiceFactory {
    ICommandService createService();

    ICommandService createService(String userId);

    ICommandService createService(String userId, IExecutionContext parentContext);
}

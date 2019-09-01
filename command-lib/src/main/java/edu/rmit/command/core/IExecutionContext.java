package edu.rmit.command.core;

import java.util.Date;
import java.util.UUID;

public interface IExecutionContext {
    String getUserId();

    UUID getOperationId();

    Date getStartedOn();

    int getDepth();

    ICommand getCommand();

    IServiceResolver getServiceResolver();

    ICommandService getCommandService();

    ICommandService getCommandService(String userId);
}

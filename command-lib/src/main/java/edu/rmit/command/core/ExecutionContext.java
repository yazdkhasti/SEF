package edu.rmit.command.core;

import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.UUID;


public class ExecutionContext<T extends ICommand> implements ICommandExecutionContext<T> {


    private final String userId;
    private final IExecutionContext parentContext;
    private final Date startedOn;
    private IServiceResolver serviceResolver;
    private final UUID operationId;
    private T command;
    private ICommandServiceFactory commandServiceFactory;

    public ExecutionContext(@NotNull T command, String userId, @NotNull IServiceResolver serviceResolver, ICommandServiceFactory commandServiceFactory, IExecutionContext parentContext) {

        this.userId = userId;
        this.command = command;
        this.parentContext = parentContext;
        this.serviceResolver = serviceResolver;
        this.commandServiceFactory = commandServiceFactory;
        this.startedOn = new Date();
        this.operationId = UUID.randomUUID();
    }

    @Override
    public int getDepth() {
        return 0;
    }

    @Override
    public IServiceResolver getServiceResolver() {
        return serviceResolver;
    }

    @Override
    public ICommandService getCommandService() {
        return commandServiceFactory.createService(this.userId, this);
    }

    @Override
    public ICommandService getCommandService(String userId) {
        return commandServiceFactory.createService(userId, this);
    }

    @Override
    public String getUserId() {
        return userId;
    }

    @Override
    public UUID getOperationId() {
        return operationId;
    }

    @Override
    public Date getStartedOn() {
        return startedOn;
    }

    @Override
    public T getCommand() {
        return this.command;
    }

    public void setCommand(T command) {
        this.command = command;
    }


}

package edu.rmit.command.core;


import edu.rmit.command.exception.CommandExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ResolvableType;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.CompletableFuture;

import java.util.function.Supplier;

@Service
public class CommandStore implements ICommandStore {

    private Map<String, ICommandQueue> commandQueueMap;

    @Autowired
    private IServiceResolver serviceResolver;


    public CommandStore() {
        commandQueueMap = new HashMap<>();
    }

    public <T extends ICommand> ICommandQueue getCommandQueue(Class<T> tClass) {
        String key = tClass.getName();
        if (!commandQueueMap.containsKey(key)) {
            ICommandQueue queue;
            if (tClass.isAnnotationPresent(QueuedCommand.class)) {
                queue = new CommandQueue(true);
            } else {
                queue = new CommandQueue(false);
            }
            commandQueueMap.put(key, queue);
        }
        return commandQueueMap.get(key);
    }

    @Override
    public <R, T extends ICommand<R>> CompletableFuture<R> execute(ICommandExecutionContext<T> context) {

        ICommand cmd = context.getCommand();
        ICommandQueue queue = getCommandQueue(cmd.getClass());
        Supplier<R> task = () -> {
            try {
                executeInternal(context);
            } catch (Exception e) {
                throw new CommandExecutionException(e);
            }
            return context.getCommand().getResponse();
        };
        return queue.post(task);
    }

    private <T extends ICommand> void executeInternal(ICommandExecutionContext<T> context) throws Exception {
        executePreHandlers(context);
        executeHandlers(context);
    }

    private <T extends ICommand> void executeHandlers(ICommandExecutionContext<T> context) throws Exception {
        ResolvableType handlerResolver = ResolvableType.forClassWithGenerics(ICommandHandler.class, context.getCommand().getClass());
        List<ICommandHandler<T>> handlers = serviceResolver.getServices(handlerResolver);
        List<CommandFilter> filters = serviceResolver.getServices(CommandFilter.class);
        for (CommandFilter filter : filters) {
            filter.beforeExecution(context);
        }
        for (ICommandHandler<T> handler : handlers) {
            handler.handle(context);
        }
        for (CommandFilter filter : filters) {
            filter.afterExecution(context);
        }
    }

    private <T extends ICommand> void executePreHandlers(ICommandExecutionContext<T> context) throws Exception {
        ResolvableType preHandlerResolver = ResolvableType.forClassWithGenerics(ICommandPreHandler.class, context.getCommand().getClass());
        List<ICommandHandler<T>> preHandlers = serviceResolver.getServices(preHandlerResolver);
        for (ICommandHandler<T> handler : preHandlers) {
            handler.handle(context);
        }
    }

}



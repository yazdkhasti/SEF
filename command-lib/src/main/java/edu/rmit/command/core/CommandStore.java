package edu.rmit.command.core;


import edu.rmit.command.exception.CommandExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ResolvableType;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.CompletableFuture;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Supplier;

@Service
public class CommandStore implements ICommandStore {

    private ConcurrentMap<String, ICommandQueue> commandQueueMap;

    @Autowired
    private IServiceResolver serviceResolver;


    public CommandStore() {
        commandQueueMap = new ConcurrentHashMap<>();
    }

    public <R, T extends ICommand<R>> ICommandQueue getCommandQueue(T cmd, Class<?> tClass) {

        String key = tClass.getName();

        if (tClass.isAnnotationPresent(EnableCustomKeySelector.class)) {
            ResolvableType keySelectorClass = ResolvableType.forClassWithGenerics(IQueueKeySelector.class, tClass);
            IQueueKeySelector keySelector = serviceResolver.getService(keySelectorClass);
            key = key + "#" + keySelector.getKey(cmd, tClass);
        }


        if (!commandQueueMap.containsKey(key)) {
            ICommandQueue queue;
            if (tClass.isAnnotationPresent(QueuedCommand.class)) {
                queue = new CommandQueue(true);
            } else {
                queue = new CommandQueue(false);
            }
            commandQueueMap.putIfAbsent(key, queue);
        }

        return commandQueueMap.get(key);
    }

    @Override
    public <R, T extends ICommand<R>> CompletableFuture<R> execute(ICommandExecutionContext<T> context) {

        T cmd = context.getCommand();
        ICommandQueue queue = getCommandQueue(cmd, cmd.getClass());
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

    private <T extends ICommand> void executeInternal(ICommandExecutionContext<T> context) {
        executePreHandlers(context);
        executeHandlers(context);
        executePostHandlers(context);
    }

    private <T extends ICommand> void executeHandlers(ICommandExecutionContext<T> context) {
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

    private <T extends ICommand> void executePreHandlers(ICommandExecutionContext<T> context) {
        ResolvableType preHandlerResolver = ResolvableType.forClassWithGenerics(ICommandPreHandler.class, context.getCommand().getClass());
        List<ICommandPreHandler<T>> preHandlers = serviceResolver.getServices(preHandlerResolver);
        for (ICommandPreHandler<T> handler : preHandlers) {
            handler.handle(context);
        }
    }

    private <T extends ICommand> void executePostHandlers(ICommandExecutionContext<T> context) {
        ResolvableType preHandlerResolver = ResolvableType.forClassWithGenerics(ICommandPostHandler.class, context.getCommand().getClass());
        List<ICommandPostHandler<T>> preHandlers = serviceResolver.getServices(preHandlerResolver);
        for (ICommandPostHandler<T> handler : preHandlers) {
            handler.handle(context);
        }
    }

}



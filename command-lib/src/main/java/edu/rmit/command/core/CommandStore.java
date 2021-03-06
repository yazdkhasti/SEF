package edu.rmit.command.core;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ResolvableType;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

@Service
public class CommandStore implements ICommandStore {

    private ConcurrentMap<String, ICommandQueue> commandQueueMap;
    private AtomicInteger taskCount = new AtomicInteger();
    private AtomicInteger asyncTaskCount = new AtomicInteger();

    @Autowired
    private IServiceResolver serviceResolver;


    public CommandStore() {
        commandQueueMap = new ConcurrentHashMap<>();
    }

    public <R, T extends ICommand<R>> ICommandQueue getCommandQueue(T cmd, Class<?> tClass) {

        String key;

        if (tClass.isAnnotationPresent(EnableCustomKeySelector.class)) {

            ResolvableType keySelectorClass = ResolvableType.forClassWithGenerics(IQueueKeySelector.class, tClass);
            IQueueKeySelector keySelector = serviceResolver.getService(keySelectorClass);
            key = keySelector.getKey(cmd, tClass);

        } else {

            key = tClass.getName();
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
    public int getAsyncTaskCount() {
        return asyncTaskCount.get();
    }

    @Override
    public int getTasKCount() {
        return taskCount.get();
    }

    @Override
    public <R, T extends ICommand<R>> CompletableFuture<R> execute(ICommandExecutionContext<T> context, ExecutionOptions options) {

        T cmd = context.getCommand();
        ICommandQueue queue = getCommandQueue(cmd, cmd.getClass());
        Supplier<R> task = () -> {
            try {
                taskCount.incrementAndGet();
                executeInternal(context, options, queue);
            } finally {
                taskCount.decrementAndGet();
            }
            return context.getCommand().getResponse();
        };
        return queue.post(task);

    }

    public <T extends ICommand> void executeInternal(ICommandExecutionContext<T> context, ExecutionOptions options, ICommandQueue queue) {

        executePreHandlers(context, options);
        executeHandlers(context, options);
        executePostHandlers(context, options);
        if (!options.getIgnoreAsyncHandlers()) {
            executeAsyncPostHandlers(context, options, queue);
        }

    }

    private <T extends ICommand> void executeHandlers(ICommandExecutionContext<T> context, ExecutionOptions options) {
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

    private <T extends ICommand> void executePreHandlers(ICommandExecutionContext<T> context, ExecutionOptions options) {
        ResolvableType preHandlerResolver = ResolvableType.forClassWithGenerics(ICommandPreHandler.class, context.getCommand().getClass());
        List<ICommandPreHandler<T>> preHandlers = serviceResolver.getServices(preHandlerResolver);
        for (ICommandPreHandler<T> handler : preHandlers) {
            handler.handle(context);
        }
    }

    private <T extends ICommand> void executePostHandlers(ICommandExecutionContext<T> context, ExecutionOptions options) {
        ResolvableType postHandlerResolver = ResolvableType.forClassWithGenerics(ICommandPostHandler.class, context.getCommand().getClass());
        List<ICommandPostHandler<T>> postHandlers = serviceResolver.getServices(postHandlerResolver);
        for (ICommandPostHandler<T> handler : postHandlers) {
            handler.handle(context);
        }
    }

    private <T extends ICommand> void executeAsyncPostHandlers(ICommandExecutionContext<T> context, ExecutionOptions options, ICommandQueue queue) {

        ResolvableType preHandlerResolver = ResolvableType.forClassWithGenerics(IAsyncCommandHandler.class, context.getCommand().getClass());
        List<IAsyncCommandHandler<T>> preHandlers = serviceResolver.getServices(preHandlerResolver);

        for (IAsyncCommandHandler<T> handler : preHandlers) {


            Supplier task = () -> {
                try {
                    asyncTaskCount.incrementAndGet();
                    handler.handle(context);
                } finally {
                    asyncTaskCount.decrementAndGet();
                }
                return null;
            };

            queue.post(task);

        }
    }

}



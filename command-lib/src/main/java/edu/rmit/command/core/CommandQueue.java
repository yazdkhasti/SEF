package edu.rmit.command.core;


import org.springframework.beans.factory.DisposableBean;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Supplier;


public class CommandQueue implements DisposableBean, ICommandQueue {

    private ExecutorService executor;

    public CommandQueue(boolean isSync) {
        if (isSync) {
            executor = Executors.newSingleThreadExecutor();
        } else {
            executor = Executors.newCachedThreadPool();
        }

    }

    @Override
    public <R> CompletableFuture<R> post(Supplier<R> task) {
        CommandFuture<R> commandFuture = new CommandFuture<>();
        CompletableFuture.supplyAsync(task, executor).whenComplete((r, t) -> {
            if (t != null) {
                commandFuture.completeExceptionally(t);
            } else {
                commandFuture.complete(r);
            }
        });
        return commandFuture;
    }

    @Override
    public void destroy() {
        if (!executor.isShutdown()) {
            executor.shutdown();
        }
    }
}

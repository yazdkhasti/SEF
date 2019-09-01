package edu.rmit.command.core;


import org.springframework.beans.factory.DisposableBean;


import java.util.concurrent.*;
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
        return CompletableFuture.supplyAsync(task, executor);
    }

    @Override
    public void destroy() {
        if (!executor.isShutdown()) {
            executor.shutdown();
        }
    }
}

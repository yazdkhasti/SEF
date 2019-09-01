package edu.rmit.command.core;

import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

public interface ICommandQueue {
    <R> CompletableFuture<R> post(Supplier<R> task);
}

package edu.rmit.command.core;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

public interface ICommandService {
    String getUserId();

    <R, T extends ICommand<R>> CompletableFuture<R> execute(T command, ExecutionOptions options);

    <R, T extends ICommand<R>> CompletableFuture<R> execute(T command);

}

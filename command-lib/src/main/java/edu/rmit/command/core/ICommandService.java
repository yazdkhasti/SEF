package edu.rmit.command.core;

import java.util.concurrent.CompletableFuture;

public interface ICommandService {
    String getUserId();

    <R, T extends ICommand<R>> CompletableFuture<R> Execute(T command);
}

package edu.rmit.command.core;

import edu.rmit.command.exception.CommandExecutionException;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

public class CommandFuture<T> extends CompletableFuture<T> {

    @Override
    public T join() {
        T result = null;
        try {
            result = super.join();
        } catch (CompletionException ex) {
            handleException(ex);
        }
        return result;
    }

    private void handleException(CompletionException ex) {
        Throwable cause = ex.getCause();
        if (cause != null && cause instanceof CommandExecutionException) {
            throw (CommandExecutionException) cause;
        } else {
            throw ex;
        }
    }
}

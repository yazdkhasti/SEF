package edu.rmit.command.exception;

public class CommandExecutionException extends RuntimeException {


    public CommandExecutionException() {
        this(null);
    }

    public CommandExecutionException(String msg) {
        super(msg);
    }
}

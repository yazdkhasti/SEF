package edu.rmit.command.exception;

public class CommandExecutionException extends RuntimeException {

    public CommandExecutionException(String msg) {
        super(msg);
    }

    public CommandExecutionException(Exception ex) {
        super(ex);
    }
}

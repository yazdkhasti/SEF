package edu.rmit.command.exception;

public class AppExecutionException extends RuntimeException {
    public AppExecutionException(String msg) {
        super(msg);
    }
    public AppExecutionException(Exception ex) {
        super(ex);
    }

}

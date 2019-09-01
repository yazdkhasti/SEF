package edu.rmit.command.core;

public interface ICommand<R> {
    R getResponse();

    void setResponse(R response);
}

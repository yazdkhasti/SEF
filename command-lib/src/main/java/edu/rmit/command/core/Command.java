package edu.rmit.command.core;

public class Command<T> implements ICommand<T> {
    private T response;

    @Override
    public T getResponse() {
        return this.response;
    }

    @Override
    public void setResponse(T response) {
        this.response = response;
    }
}

package edu.rmit.command.core;

public interface IQueueKeySelector<T extends ICommand> {
    String getKey(T command, Class<?> tClass);
}

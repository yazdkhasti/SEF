package edu.rmit.sef.stocktradingclient.core.event;

import java.util.function.Consumer;

public interface IEventBus {
    <T> EventSubscription subscribe(String eventName, Consumer<T> consumer);

    <T> void publish(String eventName, T eventArg);
}

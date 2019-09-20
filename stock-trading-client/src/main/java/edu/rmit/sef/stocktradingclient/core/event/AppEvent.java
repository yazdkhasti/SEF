package edu.rmit.sef.stocktradingclient.core.event;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.function.Consumer;

public class AppEvent<T> {

    private String eventName;
    private IEventBus eventBus;

    public AppEvent(IEventBus eventBus, String eventName) {
        this.eventName = eventName;
        this.eventBus = eventBus;
    }


    public void publish(T eventArgs) {
        eventBus.publish(eventName, eventArgs);
    }

    public void subscribe(Consumer<T> consumer) {
        eventBus.subscribe(eventName, consumer);
    }
}

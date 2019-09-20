package edu.rmit.sef.stocktradingclient.core.event;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Consumer;

@Component
public class EventBus implements IEventBus {

    private ConcurrentMap<String, List<Consumer>> consumers;


    public EventBus() {
        consumers = new ConcurrentHashMap<>();
    }

    public List<Consumer> getConsumerList(String eventName) {
        List<Consumer> consumerList = consumers.get(eventName);
        if (consumerList == null) {
            consumerList = new ArrayList<>();
            consumers.putIfAbsent(eventName, consumerList);
        }
        return consumerList;
    }

    @Override
    public <T> EventSubscription subscribe(String eventName, Consumer<T> consumer) {
        List<Consumer> consumerList = getConsumerList(eventName);
        consumerList.add(consumer);
        return () -> consumerList.remove(consumer);
    }

    @Override
    public <T> void publish(String eventName, T eventArg) {
        List<Consumer> consumerList = getConsumerList(eventName);
        for (Consumer<T> consumer : consumerList) {
            consumer.accept(eventArg);
        }
    }
}

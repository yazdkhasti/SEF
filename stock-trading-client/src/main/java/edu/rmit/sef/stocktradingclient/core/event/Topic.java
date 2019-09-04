package edu.rmit.sef.stocktradingclient.core.event;

import edu.rmit.sef.core.model.SocketMessage;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Consumer;

public class Topic<T> {

    private String name;
    private ConcurrentMap<String, Consumer<T>> consumers;

    public Topic(String name) {
        this.name = name;
        consumers = new ConcurrentHashMap<>();
    }

    public String getName() {
        return name;
    }

    public void handleSocketMessage(SocketMessage msg) {
        T target = SocketMessage.toObject(msg);
        for (Consumer<T> consumer : consumers.values()) {
            consumer.accept(target);
        }
    }

    public String addHandler(Consumer<T> consumer) {
        String key = UUID.randomUUID().toString();
        consumers.put(key, consumer);
        return key;
    }

    public void removeHandler(String key) {
        consumers.remove(key);
    }
}

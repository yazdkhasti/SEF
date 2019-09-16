package edu.rmit.sef.stocktradingclient.core.socket;

import edu.rmit.sef.core.model.SocketMessage;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;

import java.lang.reflect.Type;

public interface StompSocketMessageHandler<T> extends StompFrameHandler {

    @Override
    default Type getPayloadType(StompHeaders stompHeaders) {
        return SocketMessage.class;
    }

    @Override
    default void handleFrame(StompHeaders stompHeaders, Object o) {
        SocketMessage target = (SocketMessage) o;
        handle(target);
    }

    void handle(SocketMessage result);
}

package edu.rmit.sef.stocktradingclient.core.socket;

import edu.rmit.command.core.ICommand;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandler;
import org.springframework.stereotype.Component;

import java.lang.reflect.Type;

@Component
public class ClientSessionHandler implements StompSessionHandler {
    @Override
    public void afterConnected(StompSession stompSession, StompHeaders stompHeaders) {

    }

    @Override
    public void handleException(StompSession stompSession, StompCommand stompCommand, StompHeaders stompHeaders, byte[] bytes, Throwable throwable) {

    }

    @Override
    public void handleTransportError(StompSession stompSession, Throwable throwable) {

    }

    @Override
    public Type getPayloadType(StompHeaders stompHeaders) {
        return ICommand.class;
    }

    @Override
    public void handleFrame(StompHeaders stompHeaders, Object o) {

    }
}

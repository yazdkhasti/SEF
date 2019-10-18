package edu.rmit.sef.stocktradingclient.core.socket;

import edu.rmit.command.core.CommandUtil;
import edu.rmit.command.core.ICommand;
import edu.rmit.sef.core.model.SocketMessage;
import edu.rmit.sef.stocktradingclient.core.event.IEventBus;
import edu.rmit.sef.stocktradingclient.core.event.Topic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.UUID;
import java.util.concurrent.*;

@Component
public class SocketConnection {

    @Value("${edu.rmit.sef.stocktrading.server.socket}")
    private String socketAddress;

    @Value("${edu.rmit.sef.stocktrading.server.clientQueue}")
    private String clientCommandQueue;

    @Value("${edu.rmit.sef.stocktrading.server.serverQueue}")
    private String serverCommandQueue;

    @Value("${edu.rmit.sef.stocktrading.server.userEventQueue}")
    private String userEventQueue;

    @Value("${edu.rmit.sef.stocktrading.server.globalEventQueue}")
    private String globalEventQueue;

    @Value("${edu.rmit.sef.stocktrading.server.timeout}")
    private int messageTimeout;

    @Autowired
    private IEventBus eventBus;

    private StompSessionHandler stompSessionHandler;
    private StompSession stompSession;
    private WebSocketStompClient stompClient;
    private boolean isConnected;
    private String token;
    private ConcurrentMap<UUID, CompletableFuture> messageHashMap = new ConcurrentHashMap<>();
    private ConcurrentMap<String, StompSession.Subscription> topicHashMap = new ConcurrentHashMap<>();


    private WebSocketStompClient getStompClient() {
        WebSocketClient client = new StandardWebSocketClient();
        WebSocketStompClient stompClient = new WebSocketStompClient(client);
        stompClient.setMessageConverter(new MappingJackson2MessageConverter());
        return stompClient;
    }

    public void connect() throws URISyntaxException, ExecutionException, InterruptedException {
        if (!isConnected) {
            StompHeaders headers = new StompHeaders();
            if (token != null) {
                headers.add("Authorization", "Bearer " + token);
            }
            stompClient = getStompClient();
            stompSessionHandler = new ClientSessionHandler();
            stompSession = stompClient.connect(new URI(socketAddress), null, headers, stompSessionHandler).get();
            isConnected = true;
            subscribeToServer();
        }
    }

    public void disconnect() {
        token = null;
        isConnected = false;
        stompSession.disconnect();
    }

    private SocketMessage sendMessage(SocketMessage message) {
        CompletableFuture<SocketMessage> completableFuture = getCompletableFuture();
        messageHashMap.putIfAbsent(message.getMessageId(), completableFuture);
        stompSession.send(serverCommandQueue, message);
        SocketMessage result = null;
        try {
            result = completableFuture.get(messageTimeout, TimeUnit.MILLISECONDS);
        } catch (Exception ex) {
            CommandUtil.throwAppExecutionException(ex);
        }
        return result;
    }

    public <T> CompletableFuture<T> getCompletableFuture() {
        CompletableFuture<T> result = new CompletableFuture<T>();
        return result;
    }

    public <R, T extends ICommand<R>> void executeCommand(ICommand<R> command) {
        SocketMessage msg = SocketMessage.newMessage(command);
        SocketMessage respMessage = sendMessage(msg);
        R commandResp = SocketMessage.toObject(respMessage);
        command.setResponse(commandResp);
    }

    private void subscribeToServer() {

        stompSession.subscribe(clientCommandQueue, (StompSocketMessageHandler) result -> completeMessage((SocketMessage) result));

        stompSession.subscribe(userEventQueue, (StompSocketMessageHandler) result -> handleEvents(result));

        stompSession.subscribe(globalEventQueue, (StompSocketMessageHandler) result -> handleEvents(result));

    }

    private void handleEvents(SocketMessage message) {
        Object eventArgs = SocketMessage.toObject(message);
        eventBus.publish(message.getName(), eventArgs);
    }

    private void completeMessage(SocketMessage resp) {
        CompletableFuture completableFuture = messageHashMap.get(resp.getMessageId());
        completableFuture.complete(resp);
    }

    public void registerTopic(Topic topic) {
        StompSession.Subscription subscription = stompSession.subscribe(topic.getName(), (StompSocketMessageHandler) result -> {
            topic.handleSocketMessage(result);
        });

        topicHashMap.putIfAbsent(topic.getName(), subscription);
    }

    public void removeTopic(Topic topic) {
        StompSession.Subscription subscription = topicHashMap.get(topic.getName());
        subscription.unsubscribe();
        topicHashMap.remove(topic.getName());
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public boolean isConnected() {
        return isConnected;
    }


}

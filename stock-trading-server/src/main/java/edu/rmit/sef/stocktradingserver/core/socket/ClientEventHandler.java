package edu.rmit.sef.stocktradingserver.core.socket;

import edu.rmit.command.core.CommandUtil;
import edu.rmit.command.core.ICommandHandler;
import edu.rmit.command.core.NullResp;
import edu.rmit.sef.core.command.PublishEventCmd;
import edu.rmit.sef.core.model.SocketMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.SimpMessagingTemplate;


@Configuration
public class ClientEventHandler {

    @Value("${edu.rmit.sef.stocktrading.server.userEventQueue}")
    private String userEventQueue;

    @Value("${edu.rmit.sef.stocktrading.server.globalEventQueue}")
    private String globalEventQueue;

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @Bean
    public ICommandHandler<PublishEventCmd> publishEventCmdHandler() {

        return executionContext -> {

            PublishEventCmd cmd = executionContext.getCommand();

            if (cmd.getIsGlobal() && cmd.getUserId() == null) {
                CommandUtil.throwCommandExecutionException("Global events must have user id.");
            }

            SocketMessage message = SocketMessage.newMessage(cmd.getEventArg());
            message.setName(cmd.getEventName());

            if (!cmd.getIsGlobal()) {
                simpMessagingTemplate.convertAndSend(globalEventQueue, message);
            } else {
                simpMessagingTemplate.convertAndSendToUser(executionContext.getUserId(), userEventQueue, message);
            }

            cmd.setResponse(new NullResp());
        };

    }
}

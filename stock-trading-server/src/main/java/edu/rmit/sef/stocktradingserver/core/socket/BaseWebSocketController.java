package edu.rmit.sef.stocktradingserver.core.socket;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.rmit.command.core.Command;
import edu.rmit.command.core.ICommand;
import edu.rmit.command.core.ICommandService;
import edu.rmit.command.core.ICommandServiceFactory;
import edu.rmit.sef.core.command.CommandResp;
import edu.rmit.sef.core.model.SocketMessage;
import edu.rmit.sef.stocktradingserver.core.api.BaseApiController;
import edu.rmit.sef.user.command.AuthenticateCmd;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.security.Principal;

@Controller
public class BaseWebSocketController extends BaseApiController {

    @Value("${edu.rmit.sef.stocktrading.server.clientQueue}")
    private String clientQueue;

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @MessageMapping(value = "${edu.rmit.sef.stocktrading.server.serverQueue}")
    public void execute(@Payload SocketMessage msg, @AuthenticationPrincipal Principal principal) throws IOException {
        ICommand o = SocketMessage.toObject(msg);
        Object resp = getCommandService().execute((ICommand) o).join();
        simpMessagingTemplate.convertAndSendToUser(principal.getName(), clientQueue, msg.getResponse(resp));
    }


}

package edu.rmit.sef.stocktradingclient.core.socket;


import edu.rmit.command.core.*;
import edu.rmit.sef.stocktradingclient.core.command.ConnectToServerCmd;
import edu.rmit.sef.stocktradingclient.core.command.ConnectToServerResp;
import edu.rmit.sef.stocktradingclient.core.event.Topic;
import edu.rmit.sef.user.command.AuthenticateCmd;
import edu.rmit.sef.user.command.AuthenticateResp;
import edu.rmit.sef.user.command.RegisterUserCmd;
import edu.rmit.sef.user.command.RegisterUserResp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Configuration
public class ServerProxy {


    @Autowired
    private SocketConnection socketConnection;


    @Bean
    public ICommandPostHandler<AuthenticateCmd> postAuthenticateHandler() {
        return executionContext -> {
                AuthenticateCmd authenticateCmd = executionContext.getCommand();
                String token = authenticateCmd.getResponse().getToken();
                ConnectToServerCmd connectToServerCmd = new ConnectToServerCmd(token);
                executionContext.getCommandService().execute(connectToServerCmd).join();
        };
    }

    @Bean
    public ICommandHandler<ConnectToServerCmd> connectToServerCmdHandler() {
        return executionContext -> {
            try {
                ConnectToServerCmd cmd = executionContext.getCommand();
                String token = cmd.getToken();
                socketConnection.setToken(token);
                socketConnection.connect();
                List<Topic> topics = executionContext.getServiceResolver().getServices(Topic.class);
                for (Topic topic : topics) {
                    socketConnection.registerTopic(topic);
                }
                cmd.setResponse(new ConnectToServerResp());
            } catch (Exception ex) {
                CommandUtil.throwCommandExecutionException(ex);
            }
        };
    }


    @Bean
    public ICommandHandler<TestCmd> testCmdHandler() {
        return executionContext -> {
            TestCmd cmd = executionContext.getCommand();
            socketConnection.executeCommand(cmd);
        };
    }


}

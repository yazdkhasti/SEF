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
public class ServerProxyHandler {


    @Value("${edu.rmit.sef.stocktrading.server.auth}")
    private String authenticateUrl;

    @Value("${edu.rmit.sef.stocktrading.server.register}")
    private String registerUrl;


    @Autowired
    private SocketConnection socketConnection;

    private RestTemplate restTemplate;
    private HttpHeaders headers;

    public ServerProxyHandler() {
        restTemplate = new RestTemplate();
        this.headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        headers.add("Accept", "*/*");
    }

    @Bean
    public ICommandHandler<AuthenticateCmd> authenticateCmdHandler() {

        return executionContext -> {

            AuthenticateCmd cmd = executionContext.getCommand();
            HttpEntity<AuthenticateCmd> request = new HttpEntity<>(cmd, headers);
            AuthenticateResp authenticateResp = restTemplate.postForObject(authenticateUrl, request, AuthenticateResp.class);
            cmd.setResponse(authenticateResp);

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
    public ICommandHandler<RegisterUserCmd> registerCmdHandler() {

        return executionContext -> {
            RegisterUserCmd cmd = executionContext.getCommand();
            HttpEntity<RegisterUserCmd> request = new HttpEntity<>(cmd, headers);
            RegisterUserResp registerUserResp = restTemplate.postForObject(registerUrl, request, RegisterUserResp.class);
            cmd.setResponse(registerUserResp);
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

package edu.rmit.sef.stocktradingclient.core.user;

import edu.rmit.command.core.ICommandHandler;
import edu.rmit.command.core.ICommandPostHandler;
import edu.rmit.command.core.NullResp;
import edu.rmit.sef.stocktradingclient.core.socket.SocketConnection;
import edu.rmit.sef.user.command.*;
import edu.rmit.sef.user.model.SystemUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.client.RestTemplate;

@Configuration
public class UserService {

    @Value("${edu.rmit.sef.stocktrading.server.auth}")
    private String authenticateUrl;

    @Value("${edu.rmit.sef.stocktrading.server.register}")
    private String registerUrl;

    @Autowired
    private SocketConnection socketConnection;

    @Autowired
    private PermissionManager permissionManager;


    private RestTemplate restTemplate;
    private HttpHeaders headers;

    public UserService() {
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
    public ICommandHandler<RegisterUserCmd> registerCmdHandler() {

        return executionContext -> {
            RegisterUserCmd cmd = executionContext.getCommand();
            HttpEntity<RegisterUserCmd> request = new HttpEntity<>(cmd, headers);
            RegisterUserResp registerUserResp = restTemplate.postForObject(registerUrl, request, RegisterUserResp.class);
            cmd.setResponse(registerUserResp);
        };

    }


    @Bean
    public ICommandPostHandler<GetCurrentUserCmd> getCurrentUserCmdHandler() {

        return executionContext -> {
            GetCurrentUserResp resp = executionContext.getCommand().getResponse();
            SystemUser principal = resp.getUser();
            permissionManager.setCurrentUser(principal);
        };

    }

    @Bean
    public ICommandHandler<LogoutCmd> logoutHandler() {

        return executionContext -> {
            LogoutCmd cmd = executionContext.getCommand();
            socketConnection.disconnect();
            cmd.setResponse(new NullResp());
        };

    }


}

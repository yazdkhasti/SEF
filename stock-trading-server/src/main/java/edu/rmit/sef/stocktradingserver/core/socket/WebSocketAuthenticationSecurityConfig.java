package edu.rmit.sef.stocktradingserver.core.socket;


import edu.rmit.command.core.ICommandService;
import edu.rmit.command.core.ICommandServiceFactory;
import edu.rmit.sef.stocktradingserver.core.util.SecurityUtil;
import edu.rmit.sef.stocktradingserver.user.command.ValidateTokenCmd;
import edu.rmit.sef.stocktradingserver.user.command.ValidateTokenResp;
import edu.rmit.sef.user.model.SystemUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.security.Principal;
import java.util.Collections;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE + 99)
public class WebSocketAuthenticationSecurityConfig extends WebSocketConfig {

    @Autowired
    private ICommandServiceFactory commandServiceFactory;

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(new ChannelInterceptor() {
            @Override
            public Message<?> preSend(Message<?> message, MessageChannel channel) {

                StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

                if (StompCommand.CONNECT.equals(accessor.getCommand())) {

                    String[] headerValues = accessor.getNativeHeader("Authorization").toArray(new String[0]);

                    if (headerValues.length > 0) {

                        String header = headerValues[0];
                        String token = SecurityUtil.getBearerToken(header);
                        ValidateTokenCmd validateTokenCmd = new ValidateTokenCmd(token);
                        ICommandService commandService = commandServiceFactory.createService();
                        ValidateTokenResp validateTokenResp = commandService.execute(validateTokenCmd).join();
                        SystemUser user = validateTokenResp.getUser();


                        AbstractAuthenticationToken authenticationToken = SecurityUtil.getToken(user);

                        accessor.setUser(authenticationToken);

                    }


                }

                return message;
            }
        });
    }
}

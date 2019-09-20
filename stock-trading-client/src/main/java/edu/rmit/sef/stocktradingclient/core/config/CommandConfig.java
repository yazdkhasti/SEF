package edu.rmit.sef.stocktradingclient.core.config;


import edu.rmit.command.core.*;
import edu.rmit.sef.user.model.SystemUserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;


@Configuration
@ComponentScan(basePackages = "edu.rmit.command")
public class CommandConfig implements ApplicationListener<ContextRefreshedEvent> {


    @Autowired
    ICommandServiceFactory commandServiceFactory;


    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        ICommandService commandService = commandServiceFactory.createService();
        commandService.execute(new InitCmd()).join();
    }

    @Bean
    public IUserIdResolver userIdResolver() {
        return new IUserIdResolver() {
            @Override
            public String getId() {
                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                String id = null;
                if ((authentication instanceof AbstractAuthenticationToken)) {
                    SystemUserPrincipal user = (SystemUserPrincipal) authentication.getPrincipal();
                    id = user.getId();
                }
                return id;
            }
        };
    }
}

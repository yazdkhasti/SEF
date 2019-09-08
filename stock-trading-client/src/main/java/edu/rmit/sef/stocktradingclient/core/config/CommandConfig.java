package edu.rmit.sef.stocktradingclient.core.config;


import edu.rmit.command.core.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;


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
                return null;
            }
        };
    }
}

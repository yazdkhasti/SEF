package edu.rmit.sef.stocktradingclient.view;

import edu.rmit.command.core.*;
import edu.rmit.sef.stocktradingclient.core.command.ConnectToServerCmd;
import edu.rmit.sef.stocktradingclient.core.event.IEventBus;
import edu.rmit.sef.user.command.AuthenticateCmd;
import edu.rmit.sef.user.command.AuthenticateResp;
import edu.rmit.sef.user.model.SystemUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

@Controller
public class MainController {

    @Autowired
    private ICommandServiceFactory commandServiceFactory;

    @Autowired
    private IEventBus eventBus;

    public MainController() {

    }

    public void init() {
        ICommandService commandService = commandServiceFactory.createService();
        commandService.execute(new InitCmd()).join();
        AuthenticateCmd authenticateCmd = new AuthenticateCmd();
        authenticateCmd.setUsername("kandoo");
        authenticateCmd.setPassword("pwd");
        AuthenticateResp authenticateResp = commandService.execute(authenticateCmd).join();
        ConnectToServerCmd connectToServerCmd = new ConnectToServerCmd(authenticateResp.getToken());
        commandService.execute(connectToServerCmd).join();
        TestCmd testCmd = new TestCmd("test-queue");
        TestResp testResp = commandService.execute(testCmd).join();
        System.out.println(testResp.getTarget());
        System.out.println("init");

        eventBus.subscribe("test.event", (SystemUser user) -> {
            System.out.println(user.getUsername());
        });

    }


}

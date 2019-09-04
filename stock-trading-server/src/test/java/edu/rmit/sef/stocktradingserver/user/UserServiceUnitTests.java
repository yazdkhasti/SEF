package edu.rmit.sef.stocktradingserver.user;


import edu.rmit.command.core.ICommandService;
import edu.rmit.sef.stocktradingserver.core.BaseTest;
import edu.rmit.sef.user.command.AuthenticateCmd;
import edu.rmit.sef.user.command.AuthenticateResp;
import edu.rmit.sef.user.command.RegisterUserCmd;
import edu.rmit.sef.user.command.RegisterUserResp;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.CompletionException;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class UserServiceUnitTests extends BaseTest {


    @Test
    public void registerAndAuthenticateUserTest() {
        ICommandService commandService = getCommandService();

        RegisterUserCmd registerUserCmd = new RegisterUserCmd();
        registerUserCmd.setFirstName("payam");
        registerUserCmd.setLastName("yazdkhasti");
        registerUserCmd.setUsername("kandoo");
        registerUserCmd.setCompany("rmit");
        registerUserCmd.setPassword("pwd");

        RegisterUserResp registerUserResp = commandService.execute(registerUserCmd).join();

        Assert.assertNotNull(registerUserResp.getId());


        AuthenticateCmd authenticateCmd = new AuthenticateCmd();
        authenticateCmd.setUsername("kandoo");
        authenticateCmd.setPassword("pwd");


        AuthenticateResp authenticateResp = commandService.execute(authenticateCmd).join();
        Assert.assertNotNull(authenticateCmd);
        Assert.assertEquals(registerUserCmd.getFirstName(), authenticateResp.getFirstName());
        Assert.assertEquals(registerUserCmd.getLastName(), authenticateResp.getLastName());

    }

    @Test(expected = CompletionException.class)
    public void preventDuplicateUserTest() {

        ICommandService commandService = getCommandService();

        RegisterUserCmd registerUserCmd = new RegisterUserCmd();
        registerUserCmd.setFirstName("payam");
        registerUserCmd.setLastName("yazdkhasti");
        registerUserCmd.setUsername("duplicate");
        registerUserCmd.setCompany("rmit");
        registerUserCmd.setPassword("pwd");

        RegisterUserResp registerUserResp = commandService.execute(registerUserCmd).join();

        Assert.assertNotNull(registerUserResp.getId());


        RegisterUserResp registerDuplicateUserResp = commandService.execute(registerUserCmd).join();

    }

    @Test
    public void checkLastSeenOnIsUpdatedTest() {

        ICommandService commandService = getCommandService();

        RegisterUserCmd registerUserCmd = new RegisterUserCmd();
        registerUserCmd.setFirstName("payam");
        registerUserCmd.setLastName("yazdkhasti");
        registerUserCmd.setUsername("lastSeenOn");
        registerUserCmd.setCompany("rmit");
        registerUserCmd.setPassword("pwd");

        RegisterUserResp registerUserResp = commandService.execute(registerUserCmd).join();

        Assert.assertNotNull(registerUserResp.getId());


        AuthenticateCmd authenticateCmd = new AuthenticateCmd();
        authenticateCmd.setUsername("lastSeenOn");
        authenticateCmd.setPassword("pwd");

        AuthenticateResp authenticateResp = commandService.execute(authenticateCmd).join();

        Assert.assertNull(authenticateResp.getLastSeenOn());

        AuthenticateResp authenticateResp2 = commandService.execute(authenticateCmd).join();
        Assert.assertNotNull(authenticateResp2.getLastSeenOn());


        AuthenticateResp authenticateResp3 = commandService.execute(authenticateCmd).join();
        Assert.assertTrue(authenticateResp3.getLastSeenOn().getTime() > authenticateResp2.getLastSeenOn().getTime());

    }
}

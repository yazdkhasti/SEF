package edu.rmit.sef.stocktradingserver.user;


import edu.rmit.command.core.ICommandService;
import edu.rmit.command.exception.CommandExecutionException;
import edu.rmit.sef.core.command.CreateEntityResp;
import edu.rmit.sef.stocktradingserver.core.BaseTest;
import edu.rmit.sef.user.command.*;
import edu.rmit.sef.user.model.SystemUser;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class UserServiceUnitTests extends BaseTest {


    @Test
    public void registerAndAuthenticateUserTest() {
        ICommandService commandService = getCommandService();

        RegisterUserCmd registerUserCmd = new RegisterUserCmd();
        registerUserCmd.setFirstName("payam");
        registerUserCmd.setLastName("yazdkhasti");
        registerUserCmd.setUsername("yazdkhasti");
        registerUserCmd.setCompany("rmit");
        registerUserCmd.setPassword("pwd");

        CreateEntityResp registerUserResp = commandService.execute(registerUserCmd).join();

        Assert.assertNotNull(registerUserResp.getId());


        ICommandService userCommandService = getCommandService(registerUserResp.getId());


        GetCurrentUserResp getCurrentUserResp = userCommandService.execute(new GetCurrentUserCmd()).join();
        SystemUser currentUser = getCurrentUserResp.getUser();
        Assert.assertNotNull(currentUser);
        Assert.assertEquals(currentUser.getFirstName(), registerUserCmd.getFirstName());
        Assert.assertEquals(currentUser.getLastName(), registerUserCmd.getLastName());
        Assert.assertEquals(currentUser.getCompany(), registerUserCmd.getCompany());
        Assert.assertEquals(currentUser.getUsername(), registerUserCmd.getLastName());

    }

    @Test(expected = CommandExecutionException.class)
    public void preventDuplicateUserTest() {

        ICommandService commandService = getCommandService();

        RegisterUserCmd registerUserCmd = new RegisterUserCmd();
        registerUserCmd.setFirstName("payam");
        registerUserCmd.setLastName("yazdkhasti");
        registerUserCmd.setUsername("duplicate");
        registerUserCmd.setCompany("rmit");
        registerUserCmd.setPassword("pwd");

        CreateEntityResp registerUserResp = commandService.execute(registerUserCmd).join();

        Assert.assertNotNull(registerUserResp.getId());


        commandService.execute(registerUserCmd).join();

    }

    @Test(expected = CommandExecutionException.class)
    public void usernameLengthTest() {

        ICommandService commandService = getCommandService();

        RegisterUserCmd registerUserCmd = new RegisterUserCmd();
        registerUserCmd.setFirstName("payam");
        registerUserCmd.setLastName("yazdkhasti");
        registerUserCmd.setUsername("123456");
        registerUserCmd.setCompany("rmit");
        registerUserCmd.setPassword("pwd");

        commandService.execute(registerUserCmd).join();

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

        CreateEntityResp registerUserResp = commandService.execute(registerUserCmd).join();

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

package edu.rmit.sef.stocktradingserver.user;


import edu.rmit.command.core.ICommandService;
import edu.rmit.command.exception.CommandExecutionException;
import edu.rmit.sef.core.command.CreateEntityResp;
import edu.rmit.sef.core.security.Authority;
import edu.rmit.sef.stocktradingserver.core.BaseTest;
import edu.rmit.sef.stocktradingserver.user.command.TestAuthorityCmd;
import edu.rmit.sef.stocktradingserver.user.command.TestUserAuthorityCmd;
import edu.rmit.sef.user.command.*;
import edu.rmit.sef.user.model.SystemUser;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class UserServiceUnitTests extends BaseTest {


    @Test
    public void registerUserTest() {
        ICommandService commandService = getCommandService();

        RegisterUserCmd registerUserCmd = new RegisterUserCmd();
        registerUserCmd.setFirstName("payam");
        registerUserCmd.setLastName("yazdkhasti");
        registerUserCmd.setUsername("registerUserTest");
        registerUserCmd.setCompany("rmit");
        registerUserCmd.setPassword("pwd");

        List<String> authorities = new ArrayList<>();
        registerUserCmd.setAuthorities(authorities);

        CreateEntityResp registerUserResp = commandService.execute(registerUserCmd).join();

        Assert.assertNotNull(registerUserResp.getId());


        ICommandService userCommandService = getCommandService(registerUserResp.getId());


        GetCurrentUserResp getCurrentUserResp = userCommandService.execute(new GetCurrentUserCmd()).join();
        SystemUser currentUser = getCurrentUserResp.getUser();
        Assert.assertNotNull(currentUser);
        Assert.assertEquals(currentUser.getFirstName(), registerUserCmd.getFirstName());
        Assert.assertEquals(currentUser.getLastName(), registerUserCmd.getLastName());
        Assert.assertEquals(currentUser.getCompany(), registerUserCmd.getCompany());
        Assert.assertEquals(currentUser.getUsername(), registerUserCmd.getUsername());
        Assert.assertTrue(currentUser.getAuthorities().contains(Authority.USER));
    }

    @Test(expected = SecurityException.class)
    public void onlyAdminsCanAddAdminsTest() {
        ICommandService commandService = getCommandService();

        RegisterUserCmd registerUserCmd = new RegisterUserCmd();
        registerUserCmd.setFirstName("payam");
        registerUserCmd.setLastName("yazdkhasti");
        registerUserCmd.setUsername("onlyAdminsCanAddAdminsTest");
        registerUserCmd.setCompany("rmit");
        registerUserCmd.setPassword("pwd");

        List<String> authorities = new ArrayList<>();
        authorities.add(Authority.ADMIN);
        registerUserCmd.setAuthorities(authorities);

        commandService.execute(registerUserCmd).join();
    }

    @Test()
    public void addAdminUserTest() {
        ICommandService commandService = getSystemCommandService();

        RegisterUserCmd registerUserCmd = new RegisterUserCmd();
        registerUserCmd.setFirstName("payam");
        registerUserCmd.setLastName("yazdkhasti");
        registerUserCmd.setUsername("onlyAdminsCanAddAdminsTest");
        registerUserCmd.setCompany("rmit");
        registerUserCmd.setPassword("pwd");

        List<String> authorities = new ArrayList<>();
        authorities.add(Authority.ADMIN);
        registerUserCmd.setAuthorities(authorities);

        CreateEntityResp resp = commandService.execute(registerUserCmd).join();

        ICommandService userCommandService = getCommandService(resp.getId());
        GetCurrentUserResp getCurrentUserResp = userCommandService.execute(new GetCurrentUserCmd()).join();

        Assert.assertTrue(getCurrentUserResp.getUser().getAuthorities().contains(Authority.USER));
        Assert.assertTrue(getCurrentUserResp.getUser().getAuthorities().contains(Authority.ADMIN));

    }

    @Test
    public void registerAndAuthenticateUserTest() {
        ICommandService commandService = getCommandService();

        RegisterUserCmd registerUserCmd = new RegisterUserCmd();
        registerUserCmd.setFirstName("payam");
        registerUserCmd.setLastName("yazdkhasti");
        registerUserCmd.setUsername("registerAndAuthenticateUserTest");
        registerUserCmd.setCompany("rmit");
        registerUserCmd.setPassword("pwd");


        CreateEntityResp registerUserResp = commandService.execute(registerUserCmd).join();

        Assert.assertNotNull(registerUserResp.getId());


        FindUserByIdCmd findUserByIdCmd = new FindUserByIdCmd();
        findUserByIdCmd.setUserId(registerUserResp.getId());
        FindUserByIdResp findUserByIdBeforeAuthenticationResp = commandService.execute(findUserByIdCmd).join();

        Date lastSeenOnBeforeAuthentication = findUserByIdBeforeAuthenticationResp.getUser().getLastSeenOn();


        AuthenticateCmd authenticateCmd = new AuthenticateCmd();
        authenticateCmd.setUsername(registerUserCmd.getUsername());
        authenticateCmd.setPassword(registerUserCmd.getPassword());

        FindUserByIdResp findUserByIdAfterAuthenticationResp = commandService.execute(findUserByIdCmd).join();

        Date lastSeenOnAfterAuthentication = findUserByIdAfterAuthenticationResp.getUser().getLastSeenOn();

        AuthenticateResp authenticateResp = commandService.execute(authenticateCmd).join();
        Assert.assertNotNull(authenticateResp.getToken());
        Assert.assertEquals(authenticateResp.getFirstName(), registerUserCmd.getFirstName());
        Assert.assertEquals(authenticateResp.getLastName(), registerUserCmd.getLastName());
        Assert.assertEquals(lastSeenOnBeforeAuthentication, lastSeenOnAfterAuthentication);

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

    @Test(expected = SecurityException.class)
    public void adminAuthorityGuardTest() {

        ICommandService commandService = getCommandService();

        RegisterUserCmd registerUserCmd = new RegisterUserCmd();
        registerUserCmd.setFirstName("payam");
        registerUserCmd.setLastName("yazdkhasti");
        registerUserCmd.setUsername("adminAuthorityTest");
        registerUserCmd.setCompany("rmit");
        registerUserCmd.setPassword("pwd");

        CreateEntityResp registerUserResp = commandService.execute(registerUserCmd).join();

        ICommandService registeredUserCommandService = getCommandService(registerUserResp.getId());

        TestAuthorityCmd testAuthorityCmd = new TestAuthorityCmd();
        registeredUserCommandService.execute(testAuthorityCmd).join();

    }

    @Test()
    public void userAuthorityGuardTest() {

        ICommandService commandService = getCommandService();

        RegisterUserCmd registerUserCmd = new RegisterUserCmd();
        registerUserCmd.setFirstName("payam");
        registerUserCmd.setLastName("yazdkhasti");
        registerUserCmd.setUsername("adminAuthorityTest");
        registerUserCmd.setCompany("rmit");
        registerUserCmd.setPassword("pwd");

        CreateEntityResp registerUserResp = commandService.execute(registerUserCmd).join();

        ICommandService registeredUserCommandService = getCommandService(registerUserResp.getId());

        TestUserAuthorityCmd testAuthorityCmd = new TestUserAuthorityCmd();
        registeredUserCommandService.execute(testAuthorityCmd).join();

    }

    @Test(expected = SecurityException.class)
    public void userAuthorityGuardNegativeTest() {

        ICommandService commandService = getCommandService();

        RegisterUserCmd registerUserCmd = new RegisterUserCmd();
        registerUserCmd.setFirstName("payam");
        registerUserCmd.setLastName("yazdkhasti");
        registerUserCmd.setUsername("userAuthorityGuardNegativeTest");
        registerUserCmd.setCompany("rmit");
        registerUserCmd.setPassword("pwd");

        CreateEntityResp registerUserResp = commandService.execute(registerUserCmd).join();

        ICommandService registeredUserCommandService = getCommandService();

        TestUserAuthorityCmd testAuthorityCmd = new TestUserAuthorityCmd();
        registeredUserCommandService.execute(testAuthorityCmd).join();

    }


}

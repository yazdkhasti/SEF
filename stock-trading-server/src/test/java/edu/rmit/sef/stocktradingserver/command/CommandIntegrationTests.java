package edu.rmit.sef.stocktradingserver.command;


import edu.rmit.command.core.ICommandService;
import edu.rmit.command.core.ICommandServiceFactory;
import edu.rmit.command.core.TestCmd;
import edu.rmit.command.core.TestResp;
import edu.rmit.sef.stocktradingserver.core.BaseTest;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class CommandIntegrationTests extends BaseTest {


    @Test
    public void TestCommandSystem() {
        ICommandService commandService = getCommandService();
        String testValue = "test";
        TestCmd cmd = new TestCmd(testValue);
        TestResp resp = commandService.execute(cmd).join();
        Assert.assertNotNull(resp);
        Assert.assertEquals(resp.getTarget(), cmd.getTarget());
    }
}

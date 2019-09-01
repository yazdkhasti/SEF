package edu.rmit.sef.stocktradingserver.command;


import edu.rmit.command.core.ICommandService;
import edu.rmit.command.core.NullCmd;
import edu.rmit.command.core.NullResp;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class CommandIntegrationTests {

    @Autowired
    private ICommandService commandService;

    @Test
    public void TestNullCommand() {
        String testValue = "test";
        NullCmd cmd = new NullCmd(testValue);
        NullResp resp = commandService.Execute(cmd).join();
        Assert.assertNotNull(resp);
        Assert.assertEquals(resp.getTarget(), cmd.getTarget());
    }
}

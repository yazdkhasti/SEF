package edu.rmit.sef.stocktradingserver.command;


import edu.rmit.command.core.ICommandService;
import edu.rmit.command.core.TestCmd;
import edu.rmit.command.core.TestResp;
import edu.rmit.sef.stocktradingserver.core.BaseTest;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class CommandIntegrationTests extends BaseTest {


    @Test
    public void CommandSystemTest() {
        ICommandService commandService = getCommandService();
        String testValue = "test";
        TestCmd cmd = new TestCmd(testValue);
        TestResp resp = commandService.execute(cmd).join();
        Assert.assertNotNull(resp);
        Assert.assertEquals(resp.getTarget(), cmd.getTarget());
    }


    @Test
    public void QueuedCommandTest() {
        ICommandService commandService = getCommandService();

        QueuedTestCmd cmd = new QueuedTestCmd();


        for (int i = 0; i < 300; i++) {
            commandService.execute(cmd);
        }

        waitForAllTasks();

        Assert.assertEquals(cmd.getResponse().getValue(), 300);
    }

    @Test
    public void MultipleCommandSameQueueTest() {
        ICommandService commandService = getCommandService();

        QueuedTestCmd cmd = new QueuedTestCmd();


        QueuedTestCmd cmd2 = new QueuedTestCmd();


        for (int i = 0; i < 300; i++) {
            commandService.execute(cmd);
            commandService.execute(cmd2);
        }

        waitForAllTasks();

        Assert.assertEquals(cmd.getResponse().getValue(), 599);
    }

    @Test
    public void TestNotQueuedCommand() {
        ICommandService commandService = getCommandService();

        NotQueuedCmd cmd = new NotQueuedCmd();
        cmd.setValue(0);

        for (int i = 0; i < 300; i++) {
            commandService.execute(cmd);
        }

        waitForAllTasks();

        Assert.assertNotEquals(cmd.getResponse().getValue(), 599);
    }
}

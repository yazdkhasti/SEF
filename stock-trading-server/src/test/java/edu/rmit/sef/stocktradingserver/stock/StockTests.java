package edu.rmit.sef.stocktradingserver.stock;


import edu.rmit.command.core.ICommandService;
import edu.rmit.sef.stock.command.AddStockCmd;
import edu.rmit.sef.stocktradingserver.core.BaseTest;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.CompletionException;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class StockTests extends BaseTest {

    @Test
    public void addStockTest() {
        ICommandService commandService = getCommandService();

        AddStockCmd cmd = new AddStockCmd();
        cmd.setSymbol("goog");
        cmd.setName("Google");
        cmd.setPrice(100);

        commandService.execute(cmd).join();


        Assert.assertNotNull(cmd.getResponse().getId());

    }

    @Test(expected = CompletionException.class)
    public void preventDuplicateStockTest() {
        ICommandService commandService = getCommandService();

        AddStockCmd cmd = new AddStockCmd();
        cmd.setSymbol("goog");
        cmd.setName("Google");
        cmd.setPrice(100);

        commandService.execute(cmd).join();

        commandService.execute(cmd).join();


    }
}

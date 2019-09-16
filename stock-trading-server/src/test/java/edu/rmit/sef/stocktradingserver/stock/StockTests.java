package edu.rmit.sef.stocktradingserver.stock;


import edu.rmit.command.core.ICommandService;
import edu.rmit.command.core.NullResp;
import edu.rmit.sef.core.command.CreateEntityResp;
import edu.rmit.sef.stock.command.AddStockCmd;
import edu.rmit.sef.stock.command.FindStockByIdCmd;
import edu.rmit.sef.stock.command.FindStockByIdResp;
import edu.rmit.sef.stock.command.UpdateStockCmd;
import edu.rmit.sef.stock.model.Stock;
import edu.rmit.sef.stocktradingserver.core.BaseTest;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.query.Update;
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


    @Test()
    public void updateStockTest() {

        ICommandService commandService = getCommandService();

        AddStockCmd addStockCmd = new AddStockCmd();
        addStockCmd.setSymbol("goog");
        addStockCmd.setName("Google");
        addStockCmd.setPrice(100);

        CreateEntityResp createEntityResp = commandService.execute(addStockCmd).join();

        UpdateStockCmd updateStockCmd = new UpdateStockCmd();

        updateStockCmd.setId(createEntityResp.getId());
        updateStockCmd.setName("Google2");

        NullResp updateCmdResp = commandService.execute(updateStockCmd).join();


        FindStockByIdCmd findStockByIdCmd = new FindStockByIdCmd();
        findStockByIdCmd.setId(createEntityResp.getId());

        FindStockByIdResp findStockByIdResp = commandService.execute(findStockByIdCmd).join();

        Stock updatedStock = findStockByIdResp.getStock();

        Assert.assertEquals(updatedStock.getName(), updateStockCmd.getName());
        //Assert.assertEquals(updatedStock.getPrice(), updateStockCmd.getPrice());


    }

    @Test(expected = CompletionException.class)
    public void updateStockSymbolNameDuplicateTest() {

        ICommandService commandService = getCommandService();

        AddStockCmd addStockCmd = new AddStockCmd();
        addStockCmd.setSymbol("goog");
        addStockCmd.setName("Google");
        addStockCmd.setPrice(100);

        CreateEntityResp createEntityResp = commandService.execute(addStockCmd).join();

        UpdateStockCmd updateStockCmd = new UpdateStockCmd();

        updateStockCmd.setId(createEntityResp.getId());
        updateStockCmd.setSymbol("goog");



        NullResp updateCmdResp = commandService.execute(updateStockCmd).join();



    }
}

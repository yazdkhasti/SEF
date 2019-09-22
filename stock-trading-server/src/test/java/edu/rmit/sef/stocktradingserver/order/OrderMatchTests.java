package edu.rmit.sef.stocktradingserver.order;

import edu.rmit.command.core.ICommandService;
import edu.rmit.sef.order.command.CreateOrderCmd;
import edu.rmit.sef.order.model.OrderType;
import edu.rmit.sef.stocktradingserver.core.BaseTest;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class OrderMatchTests extends BaseTest {

    @Test
    public void OrderMatchTest() {
        ICommandService commandService = getCommandService();

        String stockId = addStock(300.5);
        String firstUserId = addUser();
        String secondUserId = addUser();






    }
}

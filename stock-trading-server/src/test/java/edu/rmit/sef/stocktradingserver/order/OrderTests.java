package edu.rmit.sef.stocktradingserver.order;


import edu.rmit.command.core.ICommandService;
import edu.rmit.sef.order.command.CreateOrderCmd;
import edu.rmit.sef.order.command.GetAllOrdersCmd;
import edu.rmit.sef.order.command.RemoveOrderCmd;
import edu.rmit.sef.order.model.OrderType;
import edu.rmit.sef.stocktradingserver.core.BaseTest;
import edu.rmit.sef.stocktradingserver.order.repo.OrderRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.CompletionException;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class OrderTests extends BaseTest {
    @Autowired
    private OrderRepository orderRepository;


    @Test
    public void createOrdetTest() {
        ICommandService commandService = getCommandService();

        CreateOrderCmd cmd = new CreateOrderCmd();
       // cmd.setOrderNumber("1");
        cmd.setOrderType(OrderType.Buy);
        cmd.setPrice(100);
        cmd.setQuantity(10);
        cmd.setStockSymbol("1");


        commandService.execute(cmd).join();


        Assert.assertNotNull(cmd.getResponse().getId());

    }

    @Test(expected = CompletionException.class)
    public void preventDuplicateOrderTest() {
        ICommandService commandService = getCommandService();

        CreateOrderCmd cmd = new CreateOrderCmd();
        //cmd.setOrderNumber("2");
        cmd.setOrderType(OrderType.Buy);
        cmd.setPrice(100);
        cmd.setQuantity(10);
        cmd.setStockSymbol("2");


        commandService.execute(cmd).join();

        commandService.execute(cmd).join();
    }

    @Test
    public void removeOrderTest() {
        ICommandService commandService = getCommandService();

        Assert.assertNotNull(orderRepository.findOrderByOrderNumber("1"));
        RemoveOrderCmd cmd = new RemoveOrderCmd();
        cmd.setOrderNumber("1");

        commandService.execute(cmd).join();

        Assert.assertNull(orderRepository.findOrderByOrderNumber("1"));

    }

    @Test(expected = CompletionException.class)
    public void preventNullOrderTest() {
        ICommandService commandService = getCommandService();

        RemoveOrderCmd cmd = new RemoveOrderCmd();
        cmd.setOrderNumber("3");

        commandService.execute(cmd).join();

    }


    @Test
    public void getOrderTest() {

        ICommandService commandService = getCommandService();
        CreateOrderCmd cmd = new CreateOrderCmd();
        //cmd.setOrderNumber("5");
        cmd.setOrderType(OrderType.Sell);
        cmd.setPrice(100);
        cmd.setQuantity(10);
        cmd.setStockSymbol("5");


        commandService.execute(cmd).join();
       // System.out.println("create Order: " + cmd.getOrderNumber());

        GetAllOrdersCmd cmd2 = new GetAllOrdersCmd();

//        commandService.execute(cmd2).join();
//        System.out.println("Test: " + cmd2.getOrderList().get(0).getTransactionId());
//
//        for (int i = 0; i < cmd2.getOrderList().size(); i++) {
//            System.out.println("OrderNumber: " + cmd2.getOrderList().get(i).getTransactionId());
//        }

        Assert.assertNotNull(cmd2.getResponse());

    }


}

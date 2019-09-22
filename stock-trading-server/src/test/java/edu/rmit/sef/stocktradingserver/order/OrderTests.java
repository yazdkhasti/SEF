package edu.rmit.sef.stocktradingserver.order;


import edu.rmit.command.core.ICommandService;
import edu.rmit.sef.order.command.CreateOrderCmd;
import edu.rmit.sef.order.command.GetAllOrderCmd;
import edu.rmit.sef.order.command.GetAllOrdersCmd;
import edu.rmit.sef.order.command.RemoveOrderCmd;
import edu.rmit.sef.order.model.Order;
import edu.rmit.sef.order.model.OrderType;
import edu.rmit.sef.stocktradingserver.core.BaseTest;
import edu.rmit.sef.stocktradingserver.order.repo.OrderRepository;
import edu.rmit.sef.user.command.*;
import edu.rmit.sef.user.model.SystemUser;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Example;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.CompletionException;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class OrderTests extends BaseTest {
    @Autowired
    private OrderRepository orderRepository;


    @Test
    public void createOrdetTest() {
        ICommandService commandService = getCommandService();
        String id = addStock();
        CreateOrderCmd cmd = new CreateOrderCmd();
        cmd.setOrderType(OrderType.Buy);
        cmd.setPrice(300);
        cmd.setQuantity(10);
        cmd.setStockId(id);

        commandService.execute(cmd).join();

        Assert.assertNotNull(cmd.getResponse().getId());

    }

    @Test(expected = CompletionException.class)
    public void preventDuplicateOrderTest() {
        ICommandService commandService = getCommandService();
        String id = addStock();
        CreateOrderCmd cmd = new CreateOrderCmd();
        cmd.setOrderType(OrderType.Buy);
        cmd.setPrice(300);
        cmd.setQuantity(10);
        cmd.setStockId(id);


        commandService.execute(cmd).join();

        commandService.execute(cmd).join();
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
        List<Order> list ;
        String userId = addUser();
        String id = addStock();
        ICommandService commandService = getCommandService(userId);

        Order orderExample = new Order();
        orderExample.setId(userId);
        Example<Order> example = Example.of(orderExample);
        list = orderRepository.findAll(example);

        Assert.assertEquals(list.size(),0);



        CreateOrderCmd cmd = new CreateOrderCmd();

        cmd.setOrderType(OrderType.Buy);
        cmd.setPrice(500);
        cmd.setQuantity(10);
        cmd.setStockId(id);

        commandService.execute(cmd).join();


        GetAllOrderCmd cmd2 = new GetAllOrderCmd();
        cmd2.setPageNumber(0);
        cmd2.setPageSize(10);
        commandService.execute(cmd2).join();

        System.out.println("OrderList" +cmd2.getResponse().getOrderList().get(0).getTransactionId());
        Assert.assertNotNull(cmd2.getResponse().getOrderList());

    }


}

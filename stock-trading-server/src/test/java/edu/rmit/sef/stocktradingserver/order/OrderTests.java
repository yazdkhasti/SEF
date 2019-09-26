package edu.rmit.sef.stocktradingserver.order;


import edu.rmit.command.core.ICommandService;
import edu.rmit.command.exception.CommandExecutionException;
import edu.rmit.sef.order.command.CreateOrderCmd;
import edu.rmit.sef.order.command.GetAllOrderCmd;
import edu.rmit.sef.order.model.Order;
import edu.rmit.sef.order.model.OrderType;
import edu.rmit.sef.stocktradingserver.core.BaseTest;
import edu.rmit.sef.stocktradingserver.order.repo.OrderRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Example;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class OrderTests extends BaseTest {
    @Autowired
    private OrderRepository orderRepository;


    @Test
    public void createOrdetTest() {
        String userId = addUser();
        ICommandService commandService = getCommandService(userId);
        String id = addStock(300);
        CreateOrderCmd cmd = new CreateOrderCmd();
        cmd.setOrderType(OrderType.Buy);
        cmd.setPrice(400);
        cmd.setQuantity(10);
        cmd.setStockId(id);

        commandService.execute(cmd).join();

        Assert.assertNotNull(cmd.getResponse().getId());

    }

    @Test
    public void createOrdetTest2() {
        String userId = addUser();
        ICommandService commandService = getCommandService(userId);
        String id = addStock(300);
        addPortfolio(userId,id,100);
        CreateOrderCmd cmd = new CreateOrderCmd();
        cmd.setOrderType(OrderType.Sell);
        cmd.setPrice(400);
        cmd.setQuantity(10);
        cmd.setStockId(id);

        commandService.execute(cmd).join();

        Assert.assertNotNull(cmd.getResponse().getId());

    }

    @Test(expected = CommandExecutionException.class)
    public void createInvalidOrderTest() {
        String userId = addUser();
        ICommandService commandService = getCommandService(userId);
        String id = addStock(300);
        CreateOrderCmd cmd = new CreateOrderCmd();
        cmd.setOrderType(OrderType.Sell);
        cmd.setPrice(300.1);
        cmd.setQuantity(10);
        cmd.setStockId(id);

        commandService.execute(cmd).join();

        Assert.assertNotNull(cmd.getResponse().getId());
    }

    @Test(expected = CommandExecutionException.class)
    public void createInvalidOrderTest2() {
        String userId = addUser();
        ICommandService commandService = getCommandService(userId);
        String id = addStock(200);
        CreateOrderCmd cmd = new CreateOrderCmd();
        cmd.setOrderType(OrderType.Sell);
        cmd.setPrice(400);
        cmd.setQuantity(10);
        cmd.setStockId(id);

        commandService.execute(cmd).join();

        Assert.assertNotNull(cmd.getResponse().getId());
    }



    @Test
    public void getOrderTest() {
        List<Order> list ;
        String userId = addUser();
        String id = addStock(300);
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

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
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class OrderTests extends BaseTest {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private MongoTemplate db;



    @Test
    public void createOrdetTest() {
        String userId = addUser();
        ICommandService commandService = getCommandService(userId);

        GetAllOrderCmd getAllOrderCmd = new GetAllOrderCmd();
        getAllOrderCmd.setPageNumber(0);
        getAllOrderCmd.setPageSize(10);
        commandService.execute(getAllOrderCmd).join();
        List<Order> list = getAllOrderCmd.getResponse().getOrderList();
        Assert.assertEquals(list.size(),0);

        String id = addStock(300);
        CreateOrderCmd cmd = new CreateOrderCmd();
        cmd.setOrderType(OrderType.Buy);
        cmd.setPrice(300.5);
        cmd.setQuantity(10);
        cmd.setStockId(id);

        commandService.execute(cmd).join();

        commandService.execute(getAllOrderCmd).join();
        list = getAllOrderCmd.getResponse().getOrderList();
        Assert.assertEquals(list.size(),1);

    }

    @Test
    public void createOrdetTest2() {
        String userId = addUser();
        ICommandService commandService = getCommandService(userId);

        GetAllOrderCmd getAllOrderCmd = new GetAllOrderCmd();
        getAllOrderCmd.setPageNumber(0);
        getAllOrderCmd.setPageSize(10);
        commandService.execute(getAllOrderCmd).join();
        List<Order> list = getAllOrderCmd.getResponse().getOrderList();
        Assert.assertEquals(list.size(),0);

        String id = addStock(300);
        addPortfolio(userId,id,100);
        CreateOrderCmd cmd = new CreateOrderCmd();
        cmd.setOrderType(OrderType.Sell);
        cmd.setPrice(300.5);
        cmd.setQuantity(10);
        cmd.setStockId(id);
        commandService.execute(cmd).join();


        commandService.execute(getAllOrderCmd).join();
        list = getAllOrderCmd.getResponse().getOrderList();

        Assert.assertEquals(list.size(),1);


    }

    @Test(expected = CommandExecutionException.class)
    public void createInvalidOrderTest() {
        String userId = addUser();
        ICommandService commandService = getCommandService(userId);
        String id = addStock(300);
        CreateOrderCmd cmd = new CreateOrderCmd();
        cmd.setOrderType(OrderType.Sell);
        cmd.setPrice(350);
        cmd.setQuantity(10);
        cmd.setStockId(id);

        commandService.execute(cmd).join();

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


    }



    @Test
    public void getOrderTest() {
        List<Order> list ;
        String userId = addUser();
        String id = addStock(300);
        ICommandService commandService = getCommandService(userId);

        GetAllOrderCmd getAllOrderCmd = new GetAllOrderCmd();
        getAllOrderCmd.setPageNumber(0);
        getAllOrderCmd.setPageSize(10);
        commandService.execute(getAllOrderCmd).join();

        list = getAllOrderCmd.getResponse().getOrderList();

        Assert.assertEquals(list.size(),0); //Check this user doesn't have any orders.



        CreateOrderCmd cmd = new CreateOrderCmd();

        cmd.setOrderType(OrderType.Buy);
        cmd.setPrice(500);
        cmd.setQuantity(10);
        cmd.setStockId(id);

        commandService.execute(cmd).join();

        cmd.setOrderType(OrderType.Buy);
        cmd.setPrice(600);
        cmd.setQuantity(20);
        cmd.setStockId(id);

        commandService.execute(cmd).join();


        getAllOrderCmd.setPageNumber(0);
        getAllOrderCmd.setPageSize(10);
        commandService.execute(getAllOrderCmd).join();

        list = getAllOrderCmd.getResponse().getOrderList();
        Assert.assertEquals(list.size(),2);

    }


}

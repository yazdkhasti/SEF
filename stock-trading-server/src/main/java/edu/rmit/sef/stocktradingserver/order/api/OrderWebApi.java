package edu.rmit.sef.stocktradingserver.order.api;


import edu.rmit.sef.stocktradingserver.core.api.BaseApiController;
import edu.rmit.sef.stocktradingserver.core.command.CreateEntityResp;
import edu.rmit.sef.stocktradingserver.order.command.CreateOrder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ExecutionException;

@RestController()
@RequestMapping("/order")
public class OrderWebApi extends BaseApiController {

    @GetMapping("/")
    public CreateEntityResp get() throws ExecutionException, InterruptedException {
        CreateEntityResp res = getCommandService().Execute(new CreateOrder()).get();
        return res;
    }

}

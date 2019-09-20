package edu.rmit.sef.stocktradingclient.stock;

import edu.rmit.sef.stocktradingclient.core.event.AppEvent;
import edu.rmit.sef.stocktradingclient.core.event.EventBus;
import edu.rmit.sef.stocktradingclient.core.event.IEventBus;
import edu.rmit.sef.stocktradingclient.core.event.EntityChangedArg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class StockEvents {


    private IEventBus eventBus;

    public final AppEvent<EntityChangedArg> STOCK_ADDED;
    public final AppEvent<EntityChangedArg> STOCK_UPDATED;

    public StockEvents(@Autowired EventBus eventBus) {
        this.eventBus = eventBus;
        STOCK_ADDED = new AppEvent<>(eventBus, "STOCK_ADDED");
        STOCK_UPDATED= new AppEvent<>(eventBus, "STOCK_UPDATED");
    }


}

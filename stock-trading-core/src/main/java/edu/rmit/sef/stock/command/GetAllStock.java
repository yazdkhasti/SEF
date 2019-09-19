package edu.rmit.sef.stock.command;

import edu.rmit.command.core.Command;
import edu.rmit.sef.core.command.CreateEntityResp;
import edu.rmit.sef.stock.model.Stock;
import java.util.List;

public class GetAllStock extends Command<CreateEntityResp> {

    private List<Stock> stockList;

    public List<Stock> getStockList() {
        return stockList;
    }

    public void setStockList(List<Stock> stockList) {
        this.stockList = stockList;
    }
}

package edu.rmit.sef.stock.command;

import edu.rmit.command.core.Command;
import com.sun.tools.javac.util.List;
import edu.rmit.sef.core.command.CreateEntityResp;
import edu.rmit.sef.stock.model.Stock;

public class GetAllStock extends Command<CreateEntityResp> {

    private List<Stock> stockList;

    public List<Stock> getStockList() {
        return stockList;
    }

    public void setStockList(List<Stock> stockList) {
        this.stockList = stockList;
    }
}

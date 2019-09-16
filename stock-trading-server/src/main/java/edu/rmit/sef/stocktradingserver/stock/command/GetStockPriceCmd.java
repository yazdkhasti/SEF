package edu.rmit.sef.stocktradingserver.stock.command;

import edu.rmit.command.core.Command;

public class GetStockPriceCmd extends Command<GetStockPriceResp> {


    private String stockId;


    public String getStockId() {
        return stockId;
    }

    public void setStockId(String stockId) {
        this.stockId = stockId;
    }

}

package edu.rmit.sef.stock.command;

import edu.rmit.command.core.Command;
import edu.rmit.command.core.NullResp;

public class DisableStockCmd extends Command<NullResp> {

    private String stockId;

    public String getStockId() {
        return stockId;
    }

    public void setStockId(String stockId) {
        this.stockId = stockId;
    }

}

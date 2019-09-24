package edu.rmit.sef.stock.command;

import edu.rmit.command.core.Command;
import edu.rmit.command.core.NullResp;
import edu.rmit.command.security.CommandAuthority;


@CommandAuthority()
public class UpdateStockPriceCmd extends Command<NullResp> {

    private String stockId;
    private double price;

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getStockId() {
        return stockId;
    }

    public void setStockId(String stockId) {
        this.stockId = stockId;
    }
}

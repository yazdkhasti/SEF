package edu.rmit.sef.stock.command;

import edu.rmit.command.core.Command;
import edu.rmit.command.core.NullResp;

public class UpdateStockCmd extends Command<NullResp> {

    private String id;
    private String symbol;
    private String name;
    private Double price;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
}

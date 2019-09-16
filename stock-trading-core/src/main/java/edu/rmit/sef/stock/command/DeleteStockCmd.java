package edu.rmit.sef.stock.command;

import edu.rmit.sef.core.command.CreateEntityResp;
import edu.rmit.command.core.Command;
public class DeleteStockCmd extends Command<CreateEntityResp> {
    private String symbol;

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }
}

package edu.rmit.sef.stock.command;

import edu.rmit.command.core.Command;
import edu.rmit.command.security.CommandAuthority;
import edu.rmit.sef.core.command.CreateEntityResp;
import edu.rmit.sef.core.security.Authority;


@CommandAuthority(Authority.ADMIN)
public class AddStockCmd extends Command<CreateEntityResp> {

    private String symbol;
    private String name;
    private double price;

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

    public double getPrice() {

        return price;
    }

    public void setPrice(double price) {

        this.price = price;
    }


}

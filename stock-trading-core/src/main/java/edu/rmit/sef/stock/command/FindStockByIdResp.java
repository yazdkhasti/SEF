package edu.rmit.sef.stock.command;

import edu.rmit.sef.stock.model.Stock;

public class FindStockByIdResp {

    private Stock stock;

    public Stock getStock() {
        return stock;
    }

    public void setStock(Stock stock) {
        this.stock = stock;
    }

}

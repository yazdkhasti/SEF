package edu.rmit.sef.portfolio.command;

public class UserAllStockPortfolio {

    private String StockName;
    private long Quantity;

    public void setStockName(String stockName) {
        StockName = stockName;
    }

    public void setQuantity(long quantity) {
        Quantity = quantity;
    }


    public String getStockName() {
        return StockName;
    }

    public long getQuantity() {
        return Quantity;
    }

}

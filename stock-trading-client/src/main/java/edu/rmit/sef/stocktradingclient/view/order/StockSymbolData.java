package edu.rmit.sef.stocktradingclient.view.order;

public class StockSymbolData {
    private String symbol;
    private Double price;

    public StockSymbolData(String symbol, Double price) {
        this.symbol = symbol;
        this.price = price;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        symbol = symbol;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return symbol + "   " + Double.toString(price);
    }
}

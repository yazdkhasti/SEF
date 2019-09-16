package edu.rmit.sef.portfolio.command;

import edu.rmit.sef.portfolio.model.StockPortfolio;

public class GetUserStockPortfolioResp {

    private StockPortfolio stockPortfolio;

    public GetUserStockPortfolioResp(StockPortfolio stockPortfolio) {
        this.stockPortfolio = stockPortfolio;
    }

    public GetUserStockPortfolioResp() {
    }


    public StockPortfolio getStockPortfolio() {
        return stockPortfolio;
    }

    public void setStockPortfolio(StockPortfolio stockPortfolio) {
        this.stockPortfolio = stockPortfolio;
    }
}

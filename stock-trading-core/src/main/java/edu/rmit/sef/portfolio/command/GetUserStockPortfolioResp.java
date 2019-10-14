package edu.rmit.sef.portfolio.command;

import edu.rmit.sef.portfolio.model.StockPortfolio;
import edu.rmit.sef.stock.command.GetAllStocksResp;

public class GetUserStockPortfolioResp extends GetAllStocksResp {

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

package edu.rmit.sef.portfolio.command;

import edu.rmit.command.core.Command;

public class GetUserStockPortfolioCmd extends Command<GetUserStockPortfolioResp> {

    private String userId;
    private String stockId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }


    public String getStockId() {
        return stockId;
    }

    public void setStockId(String stockId) {
        this.stockId = stockId;
    }

}

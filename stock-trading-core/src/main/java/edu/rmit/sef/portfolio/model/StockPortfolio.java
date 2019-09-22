package edu.rmit.sef.portfolio.model;

import edu.rmit.sef.core.model.Entity;

public class StockPortfolio extends Entity {

    private String userId;
    private String stockId;
    private long quantity;

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

    public long getQuantity() {
        return quantity;
    }

    public void setQuantity(long quantity) {
        this.quantity = quantity;
    }


}

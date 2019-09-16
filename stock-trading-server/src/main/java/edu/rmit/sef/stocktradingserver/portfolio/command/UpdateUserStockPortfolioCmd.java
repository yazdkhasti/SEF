package edu.rmit.sef.stocktradingserver.portfolio.command;

import edu.rmit.command.core.Command;
import edu.rmit.command.core.NullResp;
import edu.rmit.command.core.QueuedCommand;

@QueuedCommand
public class UpdateUserStockPortfolioCmd extends Command<NullResp> {

    private String userId;
    private String stockId;
    private long quantityChanged;

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

    public long getQuantityChanged() {
        return quantityChanged;
    }

    public void setQuantityChanged(long quantityChanged) {
        this.quantityChanged = quantityChanged;
    }
}

package edu.rmit.sef.stock.command;

import edu.rmit.command.core.Command;
import edu.rmit.command.core.NullResp;
import edu.rmit.command.security.CommandAuthority;
import edu.rmit.sef.core.security.Authority;

@CommandAuthority(Authority.ADMIN)
public class ApproveStockCmd extends Command<NullResp> {
    private String stockId;

    public String getStockId() {
        return stockId;
    }

    public void setStockId(String stockId) {
        this.stockId = stockId;
    }


}

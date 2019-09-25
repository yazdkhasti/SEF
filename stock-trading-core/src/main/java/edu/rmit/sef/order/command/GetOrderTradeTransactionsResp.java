package edu.rmit.sef.order.command;

import edu.rmit.sef.order.model.TradeTransaction;

import java.util.List;

public class GetOrderTradeTransactionsResp {

    private List<TradeTransaction> tradeTransactions;

    public List<TradeTransaction> getTradeTransactions() {
        return tradeTransactions;
    }

    public void setTradeTransactions(List<TradeTransaction> tradeTransactions) {
        this.tradeTransactions = tradeTransactions;
    }


}

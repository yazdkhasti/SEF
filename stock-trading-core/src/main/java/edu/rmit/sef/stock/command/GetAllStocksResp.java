package edu.rmit.sef.stock.command;

import edu.rmit.sef.core.command.GetAllResp;
import edu.rmit.sef.stock.model.Stock;

import java.util.List;

public class GetAllStocksResp extends GetAllResp<Stock> {

    public List<Stock> getResult() {
        return result;
    }

    public void setResult(List<Stock> result) {
        this.result = result;
    }

    private List<Stock> result;


}

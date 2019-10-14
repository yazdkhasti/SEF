package edu.rmit.sef.portfolio.command;


import java.util.List;

public class GetUserAllStockPortfolioResp {

    private List<UserAllStockPortfolio> result;

    public void setResult(List<UserAllStockPortfolio> result) {
        this.result = result;
    }

    public List<UserAllStockPortfolio> getResult(){
        return result;
    }


}

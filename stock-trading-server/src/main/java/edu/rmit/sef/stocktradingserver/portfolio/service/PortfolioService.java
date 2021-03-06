package edu.rmit.sef.stocktradingserver.portfolio.service;

import edu.rmit.command.core.CommandUtil;
import edu.rmit.command.core.ICommandHandler;
import edu.rmit.command.core.NullResp;
import edu.rmit.sef.core.model.Entity;
import edu.rmit.sef.portfolio.command.GetUserStockPortfolioCmd;
import edu.rmit.sef.portfolio.command.GetUserStockPortfolioResp;
import edu.rmit.sef.portfolio.model.StockPortfolio;
import edu.rmit.sef.stock.command.FindStockByIdCmd;
import edu.rmit.sef.stock.command.FindStockByIdResp;
import edu.rmit.sef.stock.model.Stock;
import edu.rmit.sef.stocktradingserver.portfolio.command.UpdateUserStockPortfolioCmd;
import edu.rmit.sef.stocktradingserver.portfolio.repo.PortfolioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

@Configuration
public class PortfolioService {

    @Autowired
    private MongoTemplate db;

    @Autowired
    PortfolioRepository portfolioRepository;


    @Bean
    public ICommandHandler<UpdateUserStockPortfolioCmd> updateUserStockPortfolioHandler() {

        return executionContext -> {

            UpdateUserStockPortfolioCmd cmd = executionContext.getCommand();

            CommandUtil.must(() -> cmd.getQuantityChanged() != 0, "Quantity changed cannot be zero.");

            GetUserStockPortfolioCmd getUserStockPortfolioCmd = new GetUserStockPortfolioCmd();
            getUserStockPortfolioCmd.setStockId(cmd.getStockId());
            getUserStockPortfolioCmd.setUserId(cmd.getUserId());

            GetUserStockPortfolioResp getUserStockPortfolioResp = executionContext.getCommandService()
                    .execute(getUserStockPortfolioCmd)
                    .join();

            StockPortfolio stockPortfolio = getUserStockPortfolioResp.getStockPortfolio();

            long newQuantity = stockPortfolio.getQuantity() + cmd.getQuantityChanged();


            CommandUtil.must(() -> newQuantity >= 0, "Stock quantity owned by a client cannot be less than zero.");

            stockPortfolio.setQuantity(newQuantity);
            stockPortfolio.update(executionContext.getUserId());

            portfolioRepository.save(stockPortfolio);

            cmd.setResponse(new NullResp());

        };

    }

    @Bean
    public ICommandHandler<GetUserStockPortfolioCmd> getUserStockPortfolioHandler() {

        return executionContext -> {

            GetUserStockPortfolioCmd cmd = executionContext.getCommand();

            StockPortfolio stockPortfolio = findUserStockPortfolio(cmd.getUserId(), cmd.getStockId());

            if (stockPortfolio == null) {

                FindStockByIdCmd findStockByIdCmd = new FindStockByIdCmd();
                findStockByIdCmd.setId(cmd.getStockId());

                FindStockByIdResp findStockByIdResp = executionContext.getCommandService()
                        .execute(findStockByIdCmd)
                        .join();

                Stock stock = findStockByIdResp.getStock();

                stockPortfolio = Entity.newEntity(executionContext.getUserId(), StockPortfolio.class);
                stockPortfolio.setStockId(stock.getId());
                stockPortfolio.setUserId(cmd.getUserId());
                stockPortfolio.setQuantity(0);

                portfolioRepository.insert(stockPortfolio);

            }

            cmd.setResponse(new GetUserStockPortfolioResp(stockPortfolio));

        };

    }


    private StockPortfolio findUserStockPortfolio(String userId, String stockId) {

        Criteria criteria = Criteria.where("userId").is(userId);

        criteria.andOperator(Criteria.where("stockId").is(stockId));

        Query query = Query.query(criteria);

        StockPortfolio portfolio = db.findOne(query, StockPortfolio.class);

        return portfolio;
    }


}

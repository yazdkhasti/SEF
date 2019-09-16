package edu.rmit.sef.stocktradingserver.portfolio.repo;

import edu.rmit.sef.portfolio.model.StockPortfolio;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PortfolioRepository extends MongoRepository<StockPortfolio, String> {

}

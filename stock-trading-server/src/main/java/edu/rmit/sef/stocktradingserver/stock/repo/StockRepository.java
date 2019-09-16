package edu.rmit.sef.stocktradingserver.stock.repo;

import edu.rmit.sef.stock.model.Stock;
import edu.rmit.sef.user.model.SystemUser;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface StockRepository extends MongoRepository<Stock, String> {

    Stock findStockBySymbol(String symbol);
}

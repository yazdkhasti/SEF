package edu.rmit.sef.stocktradingserver.market.repo;

import edu.rmit.sef.market.model.Market;
import edu.rmit.sef.stock.model.Stock;
import edu.rmit.sef.user.model.SystemUser;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface MarketRepository extends MongoRepository<Market, String> {

    public Market findMarketByMarketID(String marketID);
}

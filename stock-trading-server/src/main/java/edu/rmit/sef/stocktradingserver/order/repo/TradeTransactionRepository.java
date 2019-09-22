package edu.rmit.sef.stocktradingserver.order.repo;

import edu.rmit.sef.order.model.TradeTransaction;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TradeTransactionRepository extends MongoRepository<TradeTransaction, String> {
}

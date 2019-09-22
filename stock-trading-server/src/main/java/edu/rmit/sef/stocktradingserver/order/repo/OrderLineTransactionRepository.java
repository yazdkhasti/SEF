package edu.rmit.sef.stocktradingserver.order.repo;

import edu.rmit.sef.order.model.OrderLineTransaction;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface OrderLineTransactionRepository extends MongoRepository<OrderLineTransaction,String> {
}

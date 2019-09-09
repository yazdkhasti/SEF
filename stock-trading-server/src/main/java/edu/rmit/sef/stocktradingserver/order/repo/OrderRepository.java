package edu.rmit.sef.stocktradingserver.order.repo;

import edu.rmit.sef.order.model.Order;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface OrderRepository extends MongoRepository<Order, String> {

}

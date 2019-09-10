package edu.rmit.sef.stocktradingserver.order.repo;

import edu.rmit.sef.order.model.Order;
import edu.rmit.sef.stock.model.Stock;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface OrderRepository extends MongoRepository<Order, String> {

     Order findOrderByOrderNumber(String orderNumber);

     List<Order> findAll();

}
package edu.rmit.sef.stocktradingserver.order.repo;

import edu.rmit.sef.order.model.Order;
import edu.rmit.sef.stock.model.Stock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface OrderRepository extends MongoRepository<Order, String> {

     Page<Order> findAllByUserID(Pageable pageable);
}
package edu.rmit.sef.stocktradingserver.order.model;

import edu.rmit.sef.core.model.Entity;

public class Order extends Entity {
   public String orderNumber;
   public long price;
   public int quantity;
}

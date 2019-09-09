package edu.rmit.sef.order.model;

import edu.rmit.sef.core.model.Entity;

public class Order extends Entity {

    private String orderNumber;
    private long price;
    private int quantity;

   public String getStockSymbol() {
      return stockSymbol;
   }

   public void setStockSymbol(String stockSymbol) {
      this.stockSymbol = stockSymbol;
   }

   private String stockSymbol;

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

}

package edu.rmit.sef.stocktradingserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.UUID;


@SpringBootApplication
public class StockTradingServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(StockTradingServerApplication.class, args);
    }

}

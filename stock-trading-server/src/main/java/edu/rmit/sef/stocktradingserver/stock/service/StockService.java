package edu.rmit.sef.stocktradingserver.stock.service;

import edu.rmit.command.core.*;
import edu.rmit.sef.core.command.CreateEntityResp;
import edu.rmit.sef.core.command.PublishEventCmd;
import edu.rmit.sef.core.model.Entity;
import edu.rmit.sef.stock.command.*;
import edu.rmit.sef.stock.model.Stock;
import edu.rmit.sef.stock.model.StockState;
import edu.rmit.sef.stocktradingserver.stock.command.GetStockPriceCmd;
import edu.rmit.sef.stocktradingserver.stock.command.GetStockPriceResp;
import edu.rmit.sef.stocktradingserver.stock.repo.StockRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Optional;

@Configuration
public class StockService {


    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Bean
    public ICommandHandler<AddStockCmd> addStockHandler() {

        return executionContext -> {

            AddStockCmd cmd = executionContext.getCommand();

            Stock stock = Entity.newEntity(executionContext.getUserId(), Stock.class);

            modelMapper.map(cmd, stock);

            checkForDuplicate(stock);

            stockRepository.insert(stock);

            cmd.setResponse(new CreateEntityResp(stock.getId()));

        };

    }

    @Bean
    public ICommandHandler<UpdateStockCmd> updateStockHandler() {

        return executionContext -> {

            UpdateStockCmd cmd = executionContext.getCommand();

            Stock stock = findStockById(cmd.getStockId());

            if (stock.getStockState() != StockState.PendingApprove) {

                stock.setName(cmd.getName());
                stock.setSymbol(cmd.getSymbol());
                stock.setPrice(cmd.getPrice());

                checkForDuplicate(stock);

                stockRepository.save(stock);

                cmd.setResponse(new NullResp());

            } else {

                CommandUtil.throwAppExecutionException("Stock details can only be updated for stock that are not on trade.");

            }

        };

    }

    @Bean
    public ICommandHandler<UpdateStockPriceCmd> updateStockPriceHandler() {

        return executionContext -> {

            UpdateStockPriceCmd cmd = executionContext.getCommand();
            ICommandService commandService = executionContext.getCommandService();

            Stock stock = findStockById(cmd.getStockId());

            stock.setPrice(cmd.getPrice());
            stockRepository.save(stock);


            StockPriceUpdatedEvent stockPriceUpdatedEvent = new StockPriceUpdatedEvent();
            stockPriceUpdatedEvent.setStock(stock);
            commandService.execute(new PublishEventCmd(stockPriceUpdatedEvent, StockEventNames.STOCK_PRICE_UPDATED));

            cmd.setResponse(new NullResp());


        };
    }

    @Bean
    public ICommandHandler<GetStockPriceCmd> getStockPriceHandler() {

        return executionContext -> {

            GetStockPriceCmd cmd = executionContext.getCommand();

            Stock stock = findStockById(cmd.getStockId());

            GetStockPriceResp resp = new GetStockPriceResp();
            resp.setPrice(stock.getPrice());

            cmd.setResponse(resp);
        };

    }

    @Bean
    public ICommandHandler<FindStockByIdCmd> findStockByIdHandler() {

        return executionContext -> {

            FindStockByIdCmd cmd = executionContext.getCommand();
            Stock stock = findStockById(cmd.getId());

            FindStockByIdResp findStockByIdResp = new FindStockByIdResp();
            findStockByIdResp.setStock(stock);

            cmd.setResponse(findStockByIdResp);
        };

    }

    private Stock findStockById(String stockId) {

        Optional<Stock> stockRecord = stockRepository.findById(stockId);
        Stock stock = null;

        if (stockRecord.isPresent()) {
            stock = stockRecord.get();
        } else {
            CommandUtil.throwRecordNotFoundException();
        }
        return stock;
    }


    private void checkForDuplicate(Stock stock) {

        Stock duplicateStock = stockRepository.findStockBySymbol(stock.getSymbol());

        if (duplicateStock != null) {
            CommandUtil.throwCommandExecutionException("Stock with same symbol already exists.");
        }

    }
}

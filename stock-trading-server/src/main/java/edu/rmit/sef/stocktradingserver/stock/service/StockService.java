package edu.rmit.sef.stocktradingserver.stock.service;

import edu.rmit.command.core.*;
import edu.rmit.sef.core.command.CreateEntityResp;
import edu.rmit.sef.core.command.GetAllResp;
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
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.awt.print.Pageable;
import java.util.List;
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

            stock.validate();

            stock.setStockState(StockState.PendingApprove);

            checkForDuplicateStockSymbol(stock.getSymbol());

            stockRepository.insert(stock);

            cmd.setResponse(new CreateEntityResp(stock.getId()));

        };

    }

    @Bean
    public ICommandHandler<UpdateStockCmd> updateStockHandler() {

        return executionContext -> {

            UpdateStockCmd cmd = executionContext.getCommand();

            Stock stock = findStockById(cmd.getStockId());

            CommandUtil.must(() -> stock.getStockState() == StockState.PendingApprove,
                    "Stock details can only be updated for stock that are not on trade.");

            stock.validate();


            if (stock.getSymbol() != cmd.getSymbol()) {
                checkForDuplicateStockSymbol(cmd.getSymbol());
            }

            stockRepository.save(stock);

            cmd.setResponse(new NullResp());

        };

    }

    @Bean
    public ICommandHandler<UpdateStockPriceCmd> updateStockPriceHandler() {

        return executionContext -> {


            UpdateStockPriceCmd cmd = executionContext.getCommand();
            ICommandService commandService = executionContext.getCommandService();

            CommandUtil.must(() -> executionContext.getUserId() == null,
                    "Stock price can only be updated by the system.");


            Stock stock = findStockById(cmd.getStockId());

            CommandUtil.must(() -> stock.getStockState() != StockState.OnTrade,
                    "Stock state must be OnTrade.");

            stock.setPrice(cmd.getPrice());

            stockRepository.save(stock);


            StockPriceUpdatedEvent stockPriceUpdatedEvent = new StockPriceUpdatedEvent();
            stockPriceUpdatedEvent.setStock(stock);
            commandService.execute(new PublishEventCmd(stockPriceUpdatedEvent, StockEventNames.STOCK_PRICE_UPDATED));

            cmd.setResponse(new NullResp());


        };
    }

    @Bean
    public ICommandHandler<ApproveStockCmd> approveStockHandler() {

        return executionContext -> {

            ApproveStockCmd cmd = executionContext.getCommand();

            Stock stock = findStockById(cmd.getStockId());

            stock.approve();

            stockRepository.save(stock);

            cmd.setResponse(new NullResp());

        };
    }

    @Bean
    public ICommandHandler<DisableStockCmd> disableStockHandler() {

        return executionContext -> {

            DisableStockCmd cmd = executionContext.getCommand();

            Stock stock = findStockById(cmd.getStockId());

            stock.disable();

            stockRepository.save(stock);

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

    @Bean
    public ICommandHandler<GetAllStocksCmd> getAllStocksHandler() {

        return executionContext -> {

            GetAllStocksCmd cmd = executionContext.getCommand();

            Stock exampleStock = new Stock();
            exampleStock.setSymbol(cmd.getFilter());
            exampleStock.setName(cmd.getFilter());

            Example<Stock> example = Example.of(exampleStock);

            Page<Stock> result = stockRepository.findAll(example, cmd.getPageable());

            GetAllResp<Stock> resp = new GetAllResp<>(result.getContent(), result.getTotalElements());

            cmd.setResponse(resp);
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


    private void checkForDuplicateStockSymbol(String stockSymbol) {

        Stock duplicateStock = stockRepository.findStockBySymbol(stockSymbol);

        if (duplicateStock != null) {
            CommandUtil.throwCommandExecutionException("Stock with same symbol already exists.");
        }

    }
}

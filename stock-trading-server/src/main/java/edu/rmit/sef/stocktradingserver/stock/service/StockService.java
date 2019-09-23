package edu.rmit.sef.stocktradingserver.stock.service;

import edu.rmit.command.core.CommandUtil;
import edu.rmit.command.core.ICommandHandler;
import edu.rmit.command.core.ICommandService;
import edu.rmit.command.core.NullResp;
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
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;
import java.util.Optional;

@Configuration
public class StockService {


    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private MongoTemplate db;

    @Autowired
    private ModelMapper modelMapper;

    @Bean
    public ICommandHandler<AddStockCmd> addStockHandler() {

        return executionContext -> {

            CommandUtil.assertNotNull(executionContext.getUserId(), "Client must be authenticated.");

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

            CommandUtil.assertNotNull(executionContext.getUserId(), "Client must be authenticated.");

            UpdateStockCmd cmd = executionContext.getCommand();

            Stock stock = findStockById(cmd.getStockId());

            if (stock.getSymbol().compareTo(cmd.getSymbol()) != 0) {
                checkForDuplicateStockSymbol(cmd.getSymbol());
            }

            modelMapper.map(cmd, stock);

            CommandUtil.must(() -> stock.getStockState() == StockState.PendingApprove,
                    "Stock details can only be updated for stock that are not on trade.");

            stock.validate();


            stockRepository.save(stock);

            cmd.setResponse(new NullResp());

        };

    }

    @Bean
    public ICommandHandler<UpdateStockPriceCmd> updateStockPriceHandler() {

        return executionContext -> {


            UpdateStockPriceCmd cmd = executionContext.getCommand();
            ICommandService commandService = executionContext.getCommandService();


            Stock stock = findStockById(cmd.getStockId());

            CommandUtil.must(() -> stock.validForUpdate(),
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

            CommandUtil.assertNotNull(executionContext.getUserId(), "Client must be authenticated.");

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

            CommandUtil.assertNotNull(executionContext.getUserId(), "Client must be authenticated.");

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

            Criteria criteria = Criteria.where("name").regex(cmd.getFilter(), "i");
            criteria.orOperator(Criteria.where("symbol").regex(cmd.getFilter(), "i"));

            Query query = Query.query(criteria);

            query.with(cmd.toPageable());


            List<Stock> result = db.find(query, Stock.class);
            long totalCount = db.count(query, Stock.class);


            GetAllStocksResp resp = new GetAllStocksResp();
            resp.setResult(result);
            resp.setTotalCount(totalCount);

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

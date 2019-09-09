package edu.rmit.sef.stocktradingserver.stock.service;

import edu.rmit.command.core.CommandUtil;
import edu.rmit.command.core.ICommandHandler;
import edu.rmit.command.core.NullResp;
import edu.rmit.sef.core.command.CreateEntityResp;
import edu.rmit.sef.core.model.Entity;
import edu.rmit.sef.stock.command.AddStockCmd;
import edu.rmit.sef.stock.command.FindStockByIdCmd;
import edu.rmit.sef.stock.command.FindStockByIdResp;
import edu.rmit.sef.stock.command.UpdateStockCmd;
import edu.rmit.sef.stock.model.Stock;
import edu.rmit.sef.stocktradingserver.stock.repo.StockRepository;
import edu.rmit.sef.stocktradingserver.user.exception.DisabledUserException;
import edu.rmit.sef.stocktradingserver.user.exception.InvalidUserCredentialsException;
import edu.rmit.sef.user.command.AuthenticateCmd;
import edu.rmit.sef.user.command.AuthenticateResp;
import edu.rmit.sef.user.model.SystemUser;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.util.Date;
import java.util.Optional;
import java.util.StringTokenizer;

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

            FindStockByIdCmd findStockByIdCmd = new FindStockByIdCmd();
            findStockByIdCmd.setId(cmd.getId());

            FindStockByIdResp findStockByIdResp = executionContext.getCommandService()
                    .execute(findStockByIdCmd)
                    .join();


            Stock stock = findStockByIdResp.getStock();

            if (stock != null) {

                if (cmd.getName() != null) {
                    stock.setName(cmd.getName());
                }
                if (cmd.getSymbol() != null) {
                    stock.setSymbol(cmd.getSymbol());
                }

                if (cmd.getPrice() != null) {
                    stock.setPrice(cmd.getPrice());
                }

                checkForDuplicate(stock);

                stockRepository.save(stock);

                cmd.setResponse(new NullResp());
            } else {
                CommandUtil.throwRecordNotFoundException();
            }

        };

    }

    @Bean
    public ICommandHandler<FindStockByIdCmd> findStockByIdHandler() {

        return executionContext -> {

            FindStockByIdCmd cmd = executionContext.getCommand();

            Optional<Stock> stockRecord = stockRepository.findById(cmd.getId());

            if (stockRecord.isPresent()) {

                FindStockByIdResp resp = new FindStockByIdResp();
                resp.setStock(stockRecord.get());

                cmd.setResponse(resp);

            }


        };

    }


    private void checkForDuplicate(Stock stock) {

        Stock duplicateStock = stockRepository.findStockBySymbol(stock.getSymbol());

        if (duplicateStock != null) {
            CommandUtil.throwCommandExecutionException("Stock with same symbol already exists.");
        }

    }
}

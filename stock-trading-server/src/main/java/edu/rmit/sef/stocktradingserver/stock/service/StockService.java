package edu.rmit.sef.stocktradingserver.stock.service;

import edu.rmit.command.core.CommandUtil;
import edu.rmit.command.core.ICommandHandler;
import edu.rmit.sef.core.command.CreateEntityResp;
import edu.rmit.sef.core.model.Entity;
import edu.rmit.sef.stock.command.AddStockCmd;
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

            Stock stock = Entity.newEntity(null, Stock.class);

            modelMapper.map(cmd, stock);

            Stock duplicateStock = stockRepository.findStockBySymbol(stock.getSymbol());

            if (duplicateStock != null) {
                CommandUtil.throwCommandExecutionException("Stock with same symbol already exists.");
            }

            stockRepository.insert(stock);

            cmd.setResponse(new CreateEntityResp(stock.getId()));


        };

    }
}

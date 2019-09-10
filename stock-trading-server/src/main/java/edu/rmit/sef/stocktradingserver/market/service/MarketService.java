package edu.rmit.sef.stocktradingserver.market.service;

import edu.rmit.command.core.CommandUtil;
import edu.rmit.command.core.ICommandHandler;
import edu.rmit.sef.core.command.CreateEntityResp;
import edu.rmit.sef.core.model.Entity;
import edu.rmit.sef.market.command.SetMarketCmd;
import edu.rmit.sef.market.model.Market;
import edu.rmit.sef.stocktradingserver.market.repo.MarketRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MarketService {


    @Autowired
    private MarketRepository marketRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Bean
    public ICommandHandler<SetMarketCmd> setMarketHandler() {

        return executionContext -> {

            SetMarketCmd cmd = executionContext.getCommand();

            Market market = Entity.newEntity(null, Market.class);

            market = marketRepository.findMarketByMarketID(cmd.getMarketID());

            modelMapper.map(cmd, market);

            if (checkTime(cmd.getStartTime()) || checkTime(cmd.getEndTime())) {
                CommandUtil.throwCommandExecutionException("Time is not valid");
            }
//
//            if (cmd.getStartTime() > cmd.getEndTime()) {
//                CommandUtil.throwCommandExecutionException("");
//            }

            marketRepository.insert(market);

            cmd.setResponse(new CreateEntityResp(market.getId()));


        };

    }

    public boolean checkTime(int time) {
        if (time < 0 || time > 24) {
            return false;
        }
        return true;
    }
}

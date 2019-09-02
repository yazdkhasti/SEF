package edu.rmit.sef.stocktradingserver.core.api;


import edu.rmit.command.core.ICommandService;
import edu.rmit.command.core.ICommandServiceFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


public abstract class BaseApiController {

    @Autowired
    private ICommandServiceFactory commandServiceFactory;


    protected ICommandService getCommandService() {
        return commandServiceFactory.createService(null);
    }

    public <R> ResponseEntity<R> ok(R response) {
        return new ResponseEntity(response, HttpStatus.OK);
    }

}

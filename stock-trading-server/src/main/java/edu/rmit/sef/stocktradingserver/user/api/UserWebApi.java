package edu.rmit.sef.stocktradingserver.user.api;


import edu.rmit.sef.stocktradingserver.user.command.AuthenticateCmd;
import edu.rmit.sef.stocktradingserver.user.command.AuthenticateResp;
import edu.rmit.sef.stocktradingserver.core.api.BaseApiController;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class UserWebApi extends BaseApiController {




    @PostMapping("/login")
    public ResponseEntity<AuthenticateResp> generateJwtToken(@RequestBody AuthenticateCmd authenticateCmd) {

        AuthenticateResp authenticateResp = getCommandService().Execute(authenticateCmd).join();

        return new ResponseEntity(authenticateResp, HttpStatus.OK);
    }


}

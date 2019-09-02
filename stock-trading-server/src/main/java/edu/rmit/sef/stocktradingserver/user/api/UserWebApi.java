package edu.rmit.sef.stocktradingserver.user.api;


import edu.rmit.sef.stocktradingserver.user.command.AuthenticateCmd;
import edu.rmit.sef.stocktradingserver.user.command.AuthenticateResp;
import edu.rmit.sef.stocktradingserver.test.core.api.BaseApiController;

import edu.rmit.sef.stocktradingserver.user.command.RegisterUserCmd;
import edu.rmit.sef.stocktradingserver.user.command.RegisterUserResp;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(path = "user")
public class UserWebApi extends BaseApiController {


    @PostMapping("/login")
    public ResponseEntity<AuthenticateResp> generateJwtToken(@RequestBody AuthenticateCmd authenticateCmd) {

        AuthenticateResp authenticateResp = getCommandService().execute(authenticateCmd).join();
        return ok(authenticateResp);

    }

    @PostMapping("/register")
    public ResponseEntity<RegisterUserResp> register(@RequestBody RegisterUserCmd vm) {
        RegisterUserResp resp = getCommandService().execute(vm).join();
        return ok(resp);

    }

}

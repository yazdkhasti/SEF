package edu.rmit.sef.stocktradingserver.user.command;

import edu.rmit.command.core.Command;

public class ValidateTokenCmd extends Command<ValidateTokenResp> {


    private String token;

    public ValidateTokenCmd(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }


}

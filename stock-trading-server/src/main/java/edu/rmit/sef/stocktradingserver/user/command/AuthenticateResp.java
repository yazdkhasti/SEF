package edu.rmit.sef.stocktradingserver.user.command;

import edu.rmit.sef.stocktradingserver.core.command.CommandResp;

public class AuthenticateResp extends CommandResp {


    private String username;
    private String token;

    public AuthenticateResp(String username, String token) {
        this.username = username;
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public String getUsername() {
        return username;
    }


}

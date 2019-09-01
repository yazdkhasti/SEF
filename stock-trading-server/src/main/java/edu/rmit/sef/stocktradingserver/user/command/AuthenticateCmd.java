package edu.rmit.sef.stocktradingserver.user.command;

import edu.rmit.command.core.Command;

public class AuthenticateCmd extends Command<AuthenticateResp> {
    private String username;
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

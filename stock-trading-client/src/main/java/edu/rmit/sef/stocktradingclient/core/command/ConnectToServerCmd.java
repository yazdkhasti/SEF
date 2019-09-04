package edu.rmit.sef.stocktradingclient.core.command;

import edu.rmit.command.core.Command;

public class ConnectToServerCmd extends Command<ConnectToServerResp> {



    private String token;

    public ConnectToServerCmd(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

}

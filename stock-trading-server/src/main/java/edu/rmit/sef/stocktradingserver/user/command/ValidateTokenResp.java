package edu.rmit.sef.stocktradingserver.user.command;

import edu.rmit.sef.user.model.SystemUser;

public class ValidateTokenResp {

    private SystemUser user;

    public ValidateTokenResp(SystemUser user) {
        this.user = user;
    }

    public SystemUser getUser() {
        return user;
    }
}

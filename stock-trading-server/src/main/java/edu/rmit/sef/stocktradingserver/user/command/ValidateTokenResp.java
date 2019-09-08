package edu.rmit.sef.stocktradingserver.user.command;


import edu.rmit.sef.user.model.SystemUserPrincipal;

public class ValidateTokenResp {

    private SystemUserPrincipal principal;

    public ValidateTokenResp(SystemUserPrincipal user) {
        this.principal = user;
    }

    public SystemUserPrincipal getUser() {
        return principal;
    }
}

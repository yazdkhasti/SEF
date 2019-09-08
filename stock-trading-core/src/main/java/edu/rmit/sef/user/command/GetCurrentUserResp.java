package edu.rmit.sef.user.command;

import edu.rmit.sef.user.model.SystemUser;

public class GetCurrentUserResp {

    private SystemUser user;

    public GetCurrentUserResp() {
    }

    public GetCurrentUserResp(SystemUser user) {
        this.user = user;
    }

    public SystemUser getUser() {
        return user;
    }
    public void setUser(SystemUser user) {
        this.user = user;
    }
}

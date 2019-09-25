package edu.rmit.sef.user.command;

import edu.rmit.sef.user.model.SystemUser;

public class FindUserByIdResp {

    private SystemUser user;

    public SystemUser getUser() {
        return user;
    }

    public void setUser(SystemUser user) {
        this.user = user;
    }

}

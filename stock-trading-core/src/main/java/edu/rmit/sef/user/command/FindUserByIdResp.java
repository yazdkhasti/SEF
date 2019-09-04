package edu.rmit.sef.user.command;

import edu.rmit.sef.user.model.SystemUser;

public class FindUserByIdResp {

    private SystemUser user;

    public FindUserByIdResp(SystemUser user) {
        this.user = user;
    }

    public SystemUser getUser() {
        return user;
    }
}

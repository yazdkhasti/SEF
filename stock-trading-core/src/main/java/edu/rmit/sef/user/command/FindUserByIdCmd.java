package edu.rmit.sef.user.command;

import edu.rmit.command.core.Command;

public class FindUserByIdCmd extends Command<FindUserByIdResp> {

    private String userId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }


}

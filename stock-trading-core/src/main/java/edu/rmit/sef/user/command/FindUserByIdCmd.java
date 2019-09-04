package edu.rmit.sef.user.command;

import edu.rmit.command.core.Command;

public class FindUserByIdCmd extends Command<FindUserByIdResp> {

    private String Id;

    public FindUserByIdCmd(String id) {
        Id = id;
    }

    public String getId() {
        return Id;
    }


}

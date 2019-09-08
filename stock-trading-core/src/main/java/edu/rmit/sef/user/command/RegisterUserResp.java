package edu.rmit.sef.user.command;

import edu.rmit.sef.core.command.CreateEntityResp;

public class RegisterUserResp extends CreateEntityResp {

    public RegisterUserResp() {
        super();
    }

    public RegisterUserResp(String id) {
        super(id);
    }
}

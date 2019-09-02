package edu.rmit.command.core;

public class InitCmd extends Command<NullResp> {

    public InitCmd() {
        this.setResponse(new NullResp());
    }
}

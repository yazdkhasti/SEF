package edu.rmit.command.core;

import java.util.function.Supplier;

public class ProcessAsyncCmd extends Command<NullResp> {

    private Runnable handler;

    public Runnable getHandler() {
        return handler;
    }

    public void setHandler(Runnable handler) {
        this.handler = handler;
    }


}

package edu.rmit.sef.stocktradingserver.command;

import edu.rmit.command.core.Command;

public class NotQueuedCmd extends Command<QueueResp> {
    private int value;

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}

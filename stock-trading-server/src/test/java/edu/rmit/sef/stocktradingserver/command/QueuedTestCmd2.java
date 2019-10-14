package edu.rmit.sef.stocktradingserver.command;

import edu.rmit.command.core.Command;
import edu.rmit.command.core.EnableCustomKeySelector;
import edu.rmit.command.core.QueuedCommand;

@QueuedCommand
@EnableCustomKeySelector
public class QueuedTestCmd2 extends Command<QueueResp> {
    private int value;

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

}

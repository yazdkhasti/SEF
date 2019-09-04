package edu.rmit.sef.core.command;

import edu.rmit.command.core.Command;
import edu.rmit.command.core.NullResp;

public class PublishEventCmd extends Command<NullResp> {

    private Object eventArg;
    private String eventName;
    private boolean isGlobal;

    public PublishEventCmd() { }

    public PublishEventCmd(Object eventArg, String eventName,boolean isGlobal) {
        setEventArg(eventArg);
        setEventName(eventName);
        setGlobal(isGlobal);
    }


    public Object getEventArg() {
        return eventArg;
    }

    public void setEventArg(Object eventArg) {
        this.eventArg = eventArg;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public boolean isGlobal() {
        return isGlobal;
    }

    public void setGlobal(boolean global) {
        isGlobal = global;
    }
}

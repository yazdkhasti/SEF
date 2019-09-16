package edu.rmit.sef.core.command;

import edu.rmit.command.core.Command;
import edu.rmit.command.core.NullResp;

public class PublishEventCmd extends Command<NullResp> {

    private Object eventArg;
    private String eventName;
    private String userId;
    private Boolean isGlobal;

    public PublishEventCmd() {
    }

    public PublishEventCmd(Object eventArg, String eventName) {
        setEventArg(eventArg);
        setEventName(eventName);
        setIsGlobal(true);
    }

    public PublishEventCmd(Object eventArg, String eventName, String userId) {
        setEventArg(eventArg);
        setEventName(eventName);
        setUserId(userId);
        setIsGlobal(false);
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


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Boolean getIsGlobal() {
        return isGlobal;
    }

    public void setIsGlobal(Boolean isGlobal) {
        this.isGlobal = isGlobal;
    }
}

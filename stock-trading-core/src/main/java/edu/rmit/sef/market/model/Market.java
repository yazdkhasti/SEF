package edu.rmit.sef.market.model;

import edu.rmit.sef.core.model.Entity;


public class Market extends Entity {

    private String marketID;
    private String status;
    private int startTime;
    private int endTime;

    public String getMarketID() {
        return marketID;
    }

    public void setMarketID(String marketID) {
        this.marketID = marketID;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getStartTime() {
        return startTime;
    }

    public void setStartTime(int startTime) {
        this.startTime = startTime;
    }

    public int getEndTime() {
        return endTime;
    }

    public void setEndTime(int endTime) {
        this.endTime = endTime;
    }
}

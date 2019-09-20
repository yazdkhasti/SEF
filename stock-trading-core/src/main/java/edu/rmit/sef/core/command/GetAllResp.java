package edu.rmit.sef.core.command;

import java.util.List;

public abstract class GetAllResp<T> {


    private long totalCount;


    public GetAllResp() {
    }


    public long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(long totalCount) {
        this.totalCount = totalCount;
    }


}

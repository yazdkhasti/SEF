package edu.rmit.sef.core.command;

import java.util.List;

public class GetAllResp<T> {

    private List<T> result;


    private long totalCount;

    public GetAllResp(List<T> result, long totalCount) {
        this.totalCount = totalCount;
        this.result = result;
    }

    public GetAllResp() {
    }

    public List<T> getResult() {
        return result;
    }

    public void setResult(List<T> result) {
        this.result = result;
    }

    public long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(long totalCount) {
        this.totalCount = totalCount;
    }


}

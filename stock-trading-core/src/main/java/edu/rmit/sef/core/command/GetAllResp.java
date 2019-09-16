package edu.rmit.sef.core.command;

import java.util.List;

public  class GetAllResp<T> {

    private List<T> result;

    public List<T> getResult() {
        return result;
    }

    public void setResult(List<T> result) {
        this.result = result;
    }

}

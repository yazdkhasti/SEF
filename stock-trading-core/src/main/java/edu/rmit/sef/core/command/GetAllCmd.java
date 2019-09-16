package edu.rmit.sef.core.command;

import edu.rmit.command.core.Command;

import java.util.List;

public class GetAllCmd<T> extends Command<GetAllResp<T>> {

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }


    private int pageNumber;
    private int pageCount;


}

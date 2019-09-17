package edu.rmit.sef.core.command;

import edu.rmit.command.core.Command;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;


public class GetAllCmd<T> extends Command<GetAllResp<T>> {

    private String filter;
    private int pageNumber;
    private int pageSize;

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }


    public Pageable getPageable() {
        return PageRequest.of(pageNumber, pageSize);
    }
}

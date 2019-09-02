package edu.rmit.sef.stocktradingserver.test.core.repo;

public class Paging {
    private int pageNumber;
    private int pageCount;

    public Paging() {
        this(1, 50);
    }

    public Paging(int pageNumber, int pageCount) {
        this.pageNumber = pageNumber;
        this.pageCount = pageCount;
    }


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


}

package com.example.lab8.repository.paging;

public class pageable {
    private int pagenumber;
    private int pagesize;

    public pageable(int pagenumber, int pagesize) {
        this.pagenumber = pagenumber;
        this.pagesize = pagesize;
    }

    public int getPagesize() {
        return pagesize;
    }

    public int getPagenumber() {
        return pagenumber;
    }
}

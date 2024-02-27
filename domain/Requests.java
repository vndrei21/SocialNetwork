package com.example.lab8.domain;

public class Requests extends Entity<Tuple<Long, Long>> {

    private RequestStatus status;

    private Long from;
    private Long to;

    public Requests(RequestStatus status, Long from, Long to) {
        this.status = status;
        this.from = from;
        this.to = to;
        this.setId(new Tuple<>(from,to));
    }

    public void setStatus(RequestStatus status) {
        this.status = status;
    }

    public RequestStatus getStatus() {
        return status;
    }

    public Long getFrom() {
        return from;
    }

    public Long getTo() {
        return to;
    }
}

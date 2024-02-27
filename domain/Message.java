package com.example.lab8.domain;

import com.example.lab8.repository.MessageDBrepository;

import java.time.LocalDateTime;
import java.util.List;

public class Message extends Entity<Long>{
    private Utilizator from;
    private List<Utilizator> to;

    private String mesagge;
    private LocalDateTime dateTime;
    private Message reply;

    public  Message(Utilizator user, List<Utilizator> utilizators, String mesagge)
    {
        this.from = user;
        this.to = utilizators;
        this.mesagge = mesagge;
        this.dateTime = LocalDateTime.now();
        this.reply = null;
    }

    public Utilizator getFrom() {
        return from;
    }

    public List<Utilizator> getTo() {
        return to;
    }

    public String getMesagge() {
        return mesagge;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setMesagge(String mesagge) {
        this.mesagge = mesagge;
    }

}

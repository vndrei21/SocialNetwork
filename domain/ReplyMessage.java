package com.example.lab8.domain;

import java.util.List;

public class ReplyMessage extends Message{
    Message  reply;
    public ReplyMessage(Utilizator user, List<Utilizator> utilizators, String mesagge,Message message) {
        super(user, utilizators, mesagge);
        this.reply = message;
    }


}

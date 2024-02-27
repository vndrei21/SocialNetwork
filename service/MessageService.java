package com.example.lab8.service;

import com.example.lab8.domain.Message;
import com.example.lab8.repository.MessageDBrepository;

public class MessageService extends AbstractService<Long, Message>{

    MessageDBrepository messageDBrepository;

    public MessageService(MessageDBrepository brepository)
    {
        messageDBrepository = brepository;
    }


    @Override
    public Iterable<Message> getAll() {
        return null;
    }
}

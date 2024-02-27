package com.example.lab8.domain.validators;

import com.example.lab8.domain.Message;

public class MessageValidator implements Validator<Message> {
    @Override
    public void validate(Message entity) throws ValidationException {
        if(entity.getTo().isEmpty())
            throw new ValidationException("Nu exista utilizatori cu care sa conversati!");
        if(entity.getMesagge().isEmpty())
            throw new ValidationException("Mesajul este prea scurt");

    }
}

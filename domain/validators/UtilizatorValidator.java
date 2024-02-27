package com.example.lab8.domain.validators;


import com.example.lab8.domain.Utilizator;

public class UtilizatorValidator extends Throwable implements Validator<Utilizator> {


    @Override
    public void validate(Utilizator entity) throws ValidationException {
        //TODO: implement method validate
        if(entity.getId()==null)
            throw new ValidationException("id-ul este null");
        if(entity.getFirstName().length() < 3)
            throw new ValidationException("Nume Invalid");
        if(entity.getLastName().length() < 3)
            throw new ValidationException("Prenume invalid");
    }
}


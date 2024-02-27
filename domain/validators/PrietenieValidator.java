package com.example.lab8.domain.validators;


import com.example.lab8.domain.Prietenie;

public class PrietenieValidator implements Validator<Prietenie> {



    @Override
    public void validate(Prietenie entity) throws ValidationException {
        if(entity.getId().getLeft() == null || entity.getId().getRight()==null)
            throw new ValidationException("Id invalid!");
        else if(entity.getId().getLeft() == entity.getId().getRight())
            throw new ValidationException("Id invalid!");

    }
}

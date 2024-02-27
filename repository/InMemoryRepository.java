package com.example.lab8.repository;



import com.example.lab8.Exceptions.EntityException;
import com.example.lab8.Exceptions.IDException;
import com.example.lab8.domain.Entity;
import com.example.lab8.domain.validators.Validator;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class InMemoryRepository<ID, E extends Entity<ID>> implements Repository<ID, E> {
    private Validator<E> validator;
    Map<ID,E> entities;

    /**
     * constructor
     * @param validator Validator validatorul de date
     */
    public InMemoryRepository(Validator<E> validator) {

        this.validator = validator;

        entities=new HashMap<ID,E>();
    }

    /**
     * cauta un element dupa id
     *
     * @param id -the id of the entity to be returned
     *           id must not be null
     * @return entitatea daca a fost gasita
     */
    @Override
    public Optional<E> findOne(ID id){
        if (id==null)
            throw new IDException("id must be not null");
        return Optional.ofNullable(entities.get(id));
    }

    /**
     * trimite toate entitatiile
     * @return lista de entitati
     */
    @Override
    public Iterable<E> findAll() {
        return entities.values();
    }

    /**
     * salveaza o entitate in lista
     *
     * @param entity E entitatea care va fi adaugata
     *               entity must be not null
     * @return entitatea daca a fost adaugata cu succes null atlfel
     */
    @Override
    public Optional<E> save(E entity) {
        if (entity==null)
            throw new EntityException("entity must be not null");
        validator.validate(entity);
        /*if(entities.get(entity.getId()) != null) {
            return entity;
        }
        else entities.put(entity.getId(),entity);
        return null;

         */
        return Optional.ofNullable(entities.putIfAbsent(entity.getId(), entity));
    }

    /***
     * stereg o entitate dupa id
     * @param id ID id-ul entitatii dorite a fi sterse
     *      id must be not null
     * @return elementul daca a fost sters cu succes null atlfel
     */
    @Override
    public Optional<E> delete(ID id) {

        if(id == null)
            throw new IDException("id must not be null");
        return Optional.ofNullable(entities.remove(id));
    }


    /**
     * @param entity entity must not be null
     * @return
     */
    @Override
    public Optional<E> update(E entity) {

        if(entity == null)
            throw new EntityException("entity must be not null!");
        validator.validate(entity);

        /*
        entities.put(entity.getId(),entity);

        if(entities.get(entity.getId()) != null) {
            entities.put(entity.getId(),entity);
            return null;
        }
        return entity;


         */
        return Optional.ofNullable(entities.putIfAbsent(entity.getId(), entity));
    }
}

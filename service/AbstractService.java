package com.example.lab8.service;


import com.example.lab8.domain.Entity;

public abstract class AbstractService<ID,E extends Entity<ID>> {


    public abstract Iterable<E> getAll();
}

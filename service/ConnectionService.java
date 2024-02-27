package com.example.lab8.service;

import com.example.lab8.domain.Utilizator;
import com.example.lab8.repository.ConnectedDBRepository;

import java.util.Optional;

public class ConnectionService extends AbstractService<Long, Utilizator> {


    private ConnectedDBRepository repository;


    public  ConnectionService(ConnectedDBRepository repository)
    {
        this.repository = repository;
    }

    /**
     * salveaza utilizatorul care s-a logat
     * @param id
     * @param first
     * @param last
     */
    public void login(Long id, String first, String last)
    {
        Utilizator user = new Utilizator(first,last);
        user.setId(id);
        repository.save(user);
    }

    public void logout()
    {
        repository.delete(5L);
    }

    @Override
    public Iterable<Utilizator> getAll() {
        return null;
    }

    public Utilizator find()
    {
        Optional<Utilizator> user1 = repository.findOne(5l);
        return user1.orElse(null);
    }

}

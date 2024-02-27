package com.example.lab8.service;


import com.example.lab8.domain.Prietenie;
import com.example.lab8.domain.Tuple;
import com.example.lab8.domain.validators.PrietenieValidator;
import com.example.lab8.repository.InMemoryRepository;
import com.example.lab8.repository.PrietenieDBRepository;
import com.example.lab8.repository.Repository;

/**
 * Service-ul pentru pr
 */
public class ServicePrieteneie extends AbstractService<Tuple<Long,Long>, Prietenie> {
     Repository<Tuple<Long,Long>, Prietenie> repository;

    /**
     * constructor
     * @param repository Inmemoryrepository repo-ul de prietenie
     */
    public ServicePrieteneie(InMemoryRepository<Tuple<Long,Long>,Prietenie> repository)
    {
        this.repository =  repository;
    }

    public ServicePrieteneie(PrietenieDBRepository prietenieDBRepository) {
        this.repository =prietenieDBRepository;
    }

    /**
     * @return toate relatiile de prietenie
     */
    public Iterable<Prietenie> getAll()
    {
        return repository.findAll();
    }

    /**
     * sterge prieteniile unui utilizator care urmeaza sa fie sters
     * @param id
     */
    public void sterge_prietenii_user(Long id) {
        InMemoryRepository<Tuple<Long,Long>,Prietenie> copie = new InMemoryRepository<>(new PrietenieValidator());
        repository.findAll().forEach(p -> {
            if (p.getId().getLeft().equals(id) || p.getId().getRight().equals(id));
            else
                copie.save(p);
        });
        this.repository = copie;
    }



}

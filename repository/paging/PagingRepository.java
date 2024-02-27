package com.example.lab8.repository.paging;

import com.example.lab8.domain.Entity;
import com.example.lab8.repository.Repository;

public interface PagingRepository<ID,E extends Entity<ID>> extends Repository<ID,E> {
    page<E> findall(pageable pageable);
}

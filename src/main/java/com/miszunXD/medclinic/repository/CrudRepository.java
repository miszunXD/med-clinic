package com.miszunXD.medclinic.repository;

import java.util.List;
import java.util.Optional;

public interface CrudRepository <T, ID> {
    List<T> findAll();

    Optional<T> findById(ID id);

    void save(T entity);

    void delete(ID id);
}

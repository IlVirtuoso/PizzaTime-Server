package com.pizzaidph2.pizzaidph2.repository;

import com.pizzaidph2.pizzaidph2.model.Account;
import com.pizzaidph2.pizzaidph2.model.Pizzeria;
import jakarta.persistence.EntityManager;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public class HybernatePizzeriaRepositoryImpl implements CrudRepository<Pizzeria, Long> {

    private EntityManager em;

    public HybernatePizzeriaRepositoryImpl(EntityManager em){
        this.em = em;
    }

    @Override
    @Transactional
    public <S extends Pizzeria> S save(S entity) {
        em.persist(entity);
        return entity;
    }

    @Override
    public <S extends Pizzeria> Iterable<S> saveAll(Iterable<S> entities) {
        return null;
    }

    @Override
    public Optional<Pizzeria> findById(Long aLong) {
        return Optional.of(em.find(Pizzeria.class, (Object)aLong));
    }

    @Override
    public boolean existsById(Long aLong) {
        return false;
    }

    @Override
    public Iterable<Pizzeria> findAll() {
        return null;
    }

    @Override
    public Iterable<Pizzeria> findAllById(Iterable<Long> longs) {
        return null;
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public void deleteById(Long aLong) {

    }

    @Override
    public void delete(Pizzeria entity) {

    }

    @Override
    public void deleteAllById(Iterable<? extends Long> longs) {

    }

    @Override
    public void deleteAll(Iterable<? extends Pizzeria> entities) {

    }

    @Override
    public void deleteAll() {

    }
}

package com.pizzaidph2.pizzaidph2.repository;

import com.pizzaidph2.pizzaidph2.model.Account;
import jakarta.persistence.EntityManager;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public class HybernateAccountRepositoryImpl implements CrudRepository<Account, Long> {

    private EntityManager em;

    public HybernateAccountRepositoryImpl(EntityManager em){
        this.em = em;
    }

    @Override
    @Transactional
    public <S extends Account> S save(S entity) {
        em.persist(entity);
        return entity;
    }

    @Override
    public <S extends Account> Iterable<S> saveAll(Iterable<S> entities) {
        return null;
    }

    @Override
    public Optional<Account> findById(Long aLong) {
        return Optional.of(em.find(Account.class, (Object)aLong));
    }

    @Override
    public boolean existsById(Long aLong) {
        return false;
    }

    @Override
    public Iterable<Account> findAll() {
        return null;
    }

    @Override
    public Iterable<Account> findAllById(Iterable<Long> longs) {
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
    public void delete(Account entity) {

    }

    @Override
    public void deleteAllById(Iterable<? extends Long> longs) {

    }

    @Override
    public void deleteAll(Iterable<? extends Account> entities) {

    }

    @Override
    public void deleteAll() {

    }
}

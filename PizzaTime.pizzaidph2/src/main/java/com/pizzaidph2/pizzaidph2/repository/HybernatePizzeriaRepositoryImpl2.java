package com.pizzaidph2.pizzaidph2.repository;

import com.pizzaidph2.pizzaidph2.model.Pizzeria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface HybernatePizzeriaRepositoryImpl2 extends JpaRepository<Pizzeria, Long> {

    //@Query("SELECT * FROM Account u WHERE u.email = :email")
    Optional<Pizzeria> findByManagerId(long managerId);
}


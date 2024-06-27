package com.pizzaidph2.pizzaidph2.repository;

import com.pizzaidph2.pizzaidph2.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface HybernateAccountRepositoryImpl2 extends JpaRepository<Account, Long> {

    //@Query("SELECT * FROM Account u WHERE u.email = :email")
    Optional<Account> findByEmail(String email);
}
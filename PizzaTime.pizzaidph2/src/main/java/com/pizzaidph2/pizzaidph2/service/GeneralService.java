package com.pizzaidph2.pizzaidph2.service;


import com.pizzaidph2.pizzaidph2.model.Account;
import com.pizzaidph2.pizzaidph2.model.Pizzeria;
import com.pizzaidph2.pizzaidph2.repository.HybernateAccountRepositoryImpl2;
import com.pizzaidph2.pizzaidph2.repository.HybernatePizzeriaRepositoryImpl2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class GeneralService {

    @Autowired
    HybernateAccountRepositoryImpl2 repo;

    @Autowired
    HybernatePizzeriaRepositoryImpl2 repoPizza;

    public Account saveUser(Account account){
        return repo.save(account);
    }

    /**
     * Return the account's UID correspondings to the email, or -1 if it not exists
     * @param email
     * @return user's ID or -1 if it's missing
     */
    public long findIDByEmail(String email) {
        Optional<Account> optAccount = repo.findByEmail(email.trim());
        if (optAccount.isPresent()) {
            return optAccount.get().getId();
        } else return -1;
    }

    /**
     * SOLO AD USO INTERNO
     * ritorna tutti i dati di un'utente
     * @param id
     * @return
     */
    public Account getInternalAccountInfo(Long id) {
        Optional<Account> target = repo.findById(id);
        if(target.isPresent())
            return target.get();
        else
            return null;
    }

    /**
     * SOLO AD USO INTERNO
     * ritorna tutti i dati di una pizzeria
     * @param id
     * @return
     */
    public Pizzeria getInternalPizzeriaInfo(Long id) {
        Optional<Pizzeria> target = repoPizza.findById(id);
        if(target.isPresent())
            return target.get();
        else
            return null;
    }

    /**
     * Return the account's UID correspondings to the email, or -1 if it not exists
     * @param id
     * @return user's ID or -1 if it's missing
     */
    public long findPizzeriaByManager(Long id) {
        Optional<Pizzeria> optPizzeria = repoPizza.findByManagerId(id);
        if (optPizzeria.isPresent()) {
            return optPizzeria.get().getId();
        } else return -1;
    }


}

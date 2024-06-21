package com.pizzatime.pizzaengine.Repository;

import com.pizzatime.pizzaengine.Model.Pizza;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HybernatePizzaRepositoryImpl extends JpaRepository<Pizza, Long> {

    //@Query("SELECT * FROM Account u WHERE u.email = :email")
    //Optional<Pizza> findByManagerId(long managerId);
}


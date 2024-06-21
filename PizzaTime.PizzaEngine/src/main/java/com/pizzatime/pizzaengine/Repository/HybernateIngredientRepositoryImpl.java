package com.pizzatime.pizzaengine.Repository;

import com.pizzatime.pizzaengine.Model.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HybernateIngredientRepositoryImpl extends JpaRepository<Ingredient, Long> {

    //@Query("SELECT * FROM Account u WHERE u.email = :email")
    //Optional<Pizza> findByManagerId(long managerId);
}


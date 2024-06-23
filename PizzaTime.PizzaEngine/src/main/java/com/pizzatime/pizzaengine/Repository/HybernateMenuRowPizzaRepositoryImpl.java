package com.pizzatime.pizzaengine.Repository;

import com.pizzatime.pizzaengine.Model.Menu;
import com.pizzatime.pizzaengine.Model.MenuRowPizza;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface HybernateMenuRowPizzaRepositoryImpl extends JpaRepository<MenuRowPizza, Long> {

    //Optional<Menu> findByPizzeriaId(Long PizzeriaId);

    //@Query("SELECT m1 FROM Menu m1 JOIN m1.pizzaRows m1r2 JOIN m1r2.pizza p WHERE p IN :pizzas GROUP BY m1 HAVING COUNT(DISTINCT p) = :size")
    //@Query("SELECT * FROM Pizza p WHERE p.email = :email")
    //List<Menu> findByPizza(@ParamHybernateMenuRowPizzaRepositoryImpl("pizzas") Set<Pizza> pizzas, @Param("size") long size);

}


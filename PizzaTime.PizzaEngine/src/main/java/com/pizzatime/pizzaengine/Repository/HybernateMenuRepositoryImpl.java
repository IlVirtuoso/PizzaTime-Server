package com.pizzatime.pizzaengine.Repository;

import com.pizzatime.pizzaengine.Model.Menu;
import com.pizzatime.pizzaengine.Model.Pizza;
import com.pizzatime.pizzaengine.Model.Seasoning;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface HybernateMenuRepositoryImpl extends JpaRepository<Menu, Long> {

    Optional<Menu> findByPizzeriaId(Long PizzeriaId);

    //@Query("SELECT m1 FROM Menu m1 JOIN m1.pizzaRows m1r2 JOIN m1r2.pizza p WHERE p IN :pizzas GROUP BY m1 HAVING COUNT(DISTINCT p) = :size")
    //@Query("SELECT * FROM Pizza p WHERE p.email = :email")
    //List<Menu> findByPizza(@Param("pizzas") Set<Pizza> pizzas, @Param("size") long size);

    @Query("SELECT m1 FROM Menu m1 JOIN m1.pizzaRows m1r2 JOIN m1r2.pizza p WHERE p.id = :pizzaId GROUP BY m1")
    List<Menu> findByPizza(@Param("pizzaId") Long pizzaId);

}


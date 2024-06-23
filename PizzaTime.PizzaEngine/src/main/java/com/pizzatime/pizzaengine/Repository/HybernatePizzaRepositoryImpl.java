package com.pizzatime.pizzaengine.Repository;

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
public interface HybernatePizzaRepositoryImpl extends JpaRepository<Pizza, Long> {

    Optional<Pizza> findByCommonName(String commonName);

    @Query("SELECT c1 FROM Pizza c1 JOIN c1.seasonings c1c2 WHERE c1c2 IN :seasoning GROUP BY c1 HAVING COUNT(DISTINCT c1c2) = :size")
    //@Query("SELECT * FROM Pizza p WHERE p.email = :email")
    List<Pizza> findBySeasonings(@Param("seasoning") Set<Seasoning> seasonings, @Param("size") long size);

    //@Query("SELECT c1 FROM Pizza c1 JOIN c1.seasonings c1c2 WHERE c1c2 IN :pizza.seasoning GROUP BY c1")
    //List<Pizza> findBySeasonings(@Param("pizza") Pizza pizza);


}


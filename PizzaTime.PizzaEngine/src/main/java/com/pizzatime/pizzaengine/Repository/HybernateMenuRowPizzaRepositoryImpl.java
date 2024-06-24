package com.pizzatime.pizzaengine.Repository;

import com.pizzatime.pizzaengine.Model.Menu;
import com.pizzatime.pizzaengine.Model.MenuRowPizza;
import com.pizzatime.pizzaengine.Model.Pizza;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface HybernateMenuRowPizzaRepositoryImpl extends JpaRepository<MenuRowPizza, Long> {

    //Optional<Menu> findByPizzeriaId(Long PizzeriaId);


}


package com.pizzatime.pizzaengine.Service;

import com.pizzatime.pizzaengine.Model.Pizza;
import com.pizzatime.pizzaengine.Repository.HybernatePizzaRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PizzaEngineService {

    @Autowired
    HybernatePizzaRepositoryImpl repoPizza;



    /** DEBUG METHODS */

    public String createPizzaDemo(){
        Pizza p = new Pizza();
        p.setCommonName("Margherita");
        p.setIngredientsList("1,2,4");
        p = repoPizza.save(p);
        return p.jsonfy();
    }




}


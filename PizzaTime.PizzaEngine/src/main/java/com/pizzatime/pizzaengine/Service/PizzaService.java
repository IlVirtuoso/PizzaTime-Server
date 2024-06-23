package com.pizzatime.pizzaengine.Service;

import com.pizzatime.pizzaengine.Component.GenericResponse;
import com.pizzatime.pizzaengine.Model.Ingredient;
import com.pizzatime.pizzaengine.Model.Pizza;
import com.pizzatime.pizzaengine.Model.Seasoning;
import com.pizzatime.pizzaengine.Repository.HybernateIngredientRepositoryImpl;
import com.pizzatime.pizzaengine.Repository.HybernatePizzaRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class PizzaService {

    @Autowired
    GenericService genService;

    @Autowired
    HybernatePizzaRepositoryImpl repoPizza;

    @Autowired
    HybernateIngredientRepositoryImpl repoIngr;

    /**
     *
     * @param name
     * @param seasonings
     * @return
     */
    public String createPizza(String name, ArrayList<Long> seasonings){
        Set<Seasoning> subset = new HashSet<Seasoning>();
        for(Long l : seasonings){
            Optional<Ingredient> searchresult = repoIngr.findById(l);
            if(searchresult.isPresent() && (searchresult.get().getClass() == (new Seasoning().getClass()))){
                subset.add((Seasoning) searchresult.get());
            }
        }
        return createPizza(name, subset);
    }


    /**
     * Servizio di creazione della pizza
     * @param name
     * @param seasonings
     * @return
     */
    public String createPizza(String name, Set<Seasoning> seasonings) {
        GenericResponse resp = new GenericResponse();
        List<Pizza> searchResult = new ArrayList<Pizza>();
        searchResult = genService.getPizzaPerfectMatch(seasonings);
        if (searchResult.isEmpty()) {

            Pizza target = new Pizza();
            Optional<Pizza> opt = repoPizza.findByCommonName(name);
            if(opt.isPresent()){
                do {
                    name = name + "_" + Math.random() * 1000;

                }while(repoPizza.findByCommonName(name).isPresent());
            }
            target.setCommonName(name);
            target.setSeasonings(seasonings);
            target = repoPizza.save(target);
            resp.setPizza(target);
            return resp.jsonfy();
        } else {
            resp.setStatusCode(GenericResponse.ALREADY_EXISTING_ITEM_CODE);
            resp.setStatusReason(GenericResponse.ALREADY_EXISTING_ITEM_MESSAGE);
            return resp.jsonfy();
        }
    }


    public String deletePizza(Long pizzaid){
        GenericResponse resp = new GenericResponse();
        Optional<Pizza> p = repoPizza.findById(pizzaid);
        if(p.isPresent()){
            repoPizza.delete(p.get());
        }
        resp.setStatusCode(GenericResponse.OK_CODE);
        resp.setStatusReason(GenericResponse.OK_MESSAGE);
        return resp.jsonfy();
    }


}

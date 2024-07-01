package com.pizzatime.pizzaengine.Service;

import com.pizzatime.pizzaengine.Component.GenericResponse;
import com.pizzatime.pizzaengine.Model.*;
import com.pizzatime.pizzaengine.Repository.HybernateIngredientRepositoryImpl;
import com.pizzatime.pizzaengine.Repository.HybernatePizzaRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class PizzaEngineService {

    @Autowired
    GenericService genService;

    @Autowired
    HybernatePizzaRepositoryImpl repoPizza;

    @Autowired
    HybernateIngredientRepositoryImpl repoIngr;





    /** DEBUG METHODS */

    @Transactional
    public String createIngredientsDemo(){
        Ingredient p = new Ingredient();
        p.setCommonName("Pomodoro");
        Set<Allergen> allergen = new HashSet<Allergen>();
        allergen.add(Allergen.VERDURA);
        allergen.add(Allergen.FRUTTA);
        p.setAllergen(allergen);
        p = repoIngr.save(p);
        return p.jsonfy();
    }


    // This method search a Margherita...
    public String searchPizzaDemo(){
        System.out.println("Start the search for a Margherita");
        Set<Seasoning> seasonings = new HashSet<Seasoning>();
        seasonings.add((Seasoning) (repoIngr.findBycommonName("Mozzarella")).get());
        seasonings.add((Seasoning) (repoIngr.findBycommonName("Pomodoro")).get());
        //seasonings.add((Seasoning) (repoIngr.findBycommonName("Basilico")).get());

        Pizza piz = new Pizza();
        piz.setSeasonings(seasonings);
        System.out.println("Size of the set: " + seasonings.size());

        List<Pizza> result =new ArrayList<Pizza>();
        result = repoPizza.findBySeasonings(seasonings, seasonings.size());

        if(!result.isEmpty()){
            for(Pizza p : result){
                System.out.println(p);
            }

        }
        System.out.println("PRECISE SEARCH without basilico");
        result = genService.getPizzaPerfectMatch(seasonings);

        if(!result.isEmpty()){
            for(Pizza p : result){
                System.out.println(p);
            }
        }else{
            System.out.println("NOTHING FOUND: OLE'");
        }
        System.out.println("PRECISE SEARCH with basilico");
        seasonings.add((Seasoning) (repoIngr.findBycommonName("Basilico")).get());
        result = genService.getPizzaPerfectMatch(seasonings);
        if(!result.isEmpty()){
            for(Pizza p : result){
                System.out.println(p);
            }
        }

        System.out.println("PRECISE SEARCH of a Marinara");
        seasonings.remove((Seasoning) (repoIngr.findBycommonName("Basilico")).get());
        seasonings.remove((Seasoning) (repoIngr.findBycommonName("Mozzarella")).get());
        result = genService.getPizzaPerfectMatch(seasonings);
        if(!result.isEmpty()){
            for(Pizza p : result){
                System.out.println(p);
            }
        }


        return "OK";
    }


}


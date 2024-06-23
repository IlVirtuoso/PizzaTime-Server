package com.pizzatime.pizzaengine.Service;


import com.pizzatime.pizzaengine.Model.Pizza;
import com.pizzatime.pizzaengine.Model.Seasoning;
import com.pizzatime.pizzaengine.Repository.HybernatePizzaRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class GenericService {


    @Autowired
    HybernatePizzaRepositoryImpl repoPizza;

    public List<Pizza> getPizzaPerfectMatch(Set<Seasoning> seasonings){

        List<Pizza> pizzaList =new ArrayList<Pizza>();
        pizzaList = repoPizza.findBySeasonings(seasonings, seasonings.size());

        List<Pizza> l = new ArrayList<Pizza>();

        for(Pizza p : pizzaList){
            Set<Seasoning> s = p.getSeasonings();
            if(seasonings.containsAll(s)){
                l.add(p);
            }
        }
        return l;
    }

    public Pizza getOnePizzaPerfectMatch(Set<Seasoning> seasonings){

        List<Pizza> pizzaList =new ArrayList<Pizza>();
        pizzaList = repoPizza.findBySeasonings(seasonings, seasonings.size());

        List<Pizza> l = new ArrayList<Pizza>();

        for(Pizza p : pizzaList){
            Set<Seasoning> s = p.getSeasonings();
            if(seasonings.containsAll(s)){
                l.add(p);
            }
        }
        if(l.size()==1){
            return l.getFirst();
        }else{
        return null;}
    }


}

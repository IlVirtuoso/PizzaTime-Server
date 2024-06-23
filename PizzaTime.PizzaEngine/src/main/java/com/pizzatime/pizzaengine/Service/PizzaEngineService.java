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


    @Transactional
    public String createIPastryDemo(){
        Ingredient p = new Pastry();
        String result = "";

        p.setCommonName("Base classica");
        p.setGlutenFree(false);
        p = repoIngr.save(p);
        result = result + p.toString() +"\n";

        p = new Pastry();
        p.setCommonName("Base classica senza glutine");
        p.setGlutenFree(true);
        p = repoIngr.save(p);
        result = result + p.toString() +"\n";

        p = new Pastry();
        p.setCommonName("Base al carbone");
        p.setGlutenFree(true);
        p = repoIngr.save(p);
        result = result + p.toString() +"\n";

        p = new Pastry();
        p.setCommonName("Base al lievito di birra");
        p.setGlutenFree(false);
        p = repoIngr.save(p);
        result = result + p.toString() +"\n";

        return result;
    }


    @Transactional
    public String createSeasoningDemo(){
        String result = "";
        Ingredient p = new Seasoning();

        p.setCommonName("Basilico");
        Set<Allergen> allergen = new HashSet<Allergen>();
        allergen.add(Allergen.VERDURA);
        p.setAllergen(allergen);
        p = repoIngr.save(p);
        result = result + p.toString() +"\n";

        p = new Seasoning();
        p.setCommonName("Pomodoro");
        allergen = new HashSet<Allergen>();
        allergen.add(Allergen.VERDURA);
        p.setAllergen(allergen);
        p = repoIngr.save(p);
        result = result + p.toString() +"\n";

        p = new Seasoning();
        p.setCommonName("Mozzarella");
        allergen = new HashSet<Allergen>();
        allergen.add(Allergen.LATTICINI);
        p.setAllergen(allergen);
        p = repoIngr.save(p);
        result = result + p.toString() +"\n";

        p = new Seasoning();
        p.setCommonName("Wurstler");
        allergen = new HashSet<Allergen>();
        allergen.add(Allergen.CARNE);
        p.setAllergen(allergen);
        p = repoIngr.save(p);
        result = result + p.toString() +"\n";

        p = new Seasoning();
        p.setCommonName("Patate Fritte");
        p = repoIngr.save(p);
        result = result + p.toString() +"\n";

        return result;
    }

    @Transactional
    public String createPizzaDemo(){
        String result = "";
        Pizza p = new Pizza();

        p.setCommonName("Margherita");
        //p.setPastry((Pastry)(repoIngr.findById(Long.parseLong("2"))).get());
        Set<Seasoning> seasonings = new HashSet<Seasoning>();
        seasonings.add((Seasoning) (repoIngr.findBycommonName("Mozzarella")).get());
        seasonings.add((Seasoning) (repoIngr.findBycommonName("Pomodoro")).get());
        seasonings.add((Seasoning) (repoIngr.findBycommonName("Basilico")).get());
        p.setSeasonings(seasonings);
        p = repoPizza.save(p);
        result = result + p.toString() +"\n";

        p = new Pizza();
        p.setCommonName("Wurstler");
        //p.setPastry((Pastry)(repoIngr.findById(Long.parseLong("2"))).get());
        seasonings = new HashSet<Seasoning>();
        seasonings.add((Seasoning) (repoIngr.findBycommonName("Mozzarella")).get());
        seasonings.add((Seasoning) (repoIngr.findBycommonName("Pomodoro")).get());
        seasonings.add((Seasoning) (repoIngr.findBycommonName("Wurstler")).get());
        p.setSeasonings(seasonings);
        p = repoPizza.save(p);
        result = result + p.toString() +"\n";

        return result;
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


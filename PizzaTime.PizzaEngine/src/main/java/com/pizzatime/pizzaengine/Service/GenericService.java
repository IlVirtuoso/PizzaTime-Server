package com.pizzatime.pizzaengine.Service;


import com.pizzatime.pizzaengine.Model.*;
import com.pizzatime.pizzaengine.Repository.HybernateIngredientRepositoryImpl;
import com.pizzatime.pizzaengine.Repository.HybernateMenuRepositoryImpl;
import com.pizzatime.pizzaengine.Repository.HybernatePizzaRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class GenericService {


    @Autowired
    HybernatePizzaRepositoryImpl repoPizza;

    @Autowired
    HybernateIngredientRepositoryImpl repoIngr;

    @Autowired
    HybernateMenuRepositoryImpl repoMenu;

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

    public Ingredient getIngredientInfoInternal(Long id){
        Optional<Ingredient> i = repoIngr.findById(id);
        if(i.isPresent()){
            return i.get();
        }else{
            return null;
        }
    }

    public Menu getMenuFromPizzeriaInfoInternal(Long pizzeriaId){
        Optional<Menu> i = repoMenu.findByPizzeriaId(pizzeriaId);
        if(i.isPresent()){
            return i.get();
        }else{
            return null;
        }
    }

    public Pizza getPizzaInfoInternal(Long pizzaId){
        Optional<Pizza> i = repoPizza.findById(pizzaId);
        if(i.isPresent()){
            return i.get();
        }else{
            return null;
        }
    }


    //POPULATE DB

    @Transactional
    public String createIPastryDemo(){
        Ingredient p = new Pastry();
        String result = "";

        if (!repoIngr.findBycommonName("Base Classica").isPresent()) {
            p.setCommonName("Base classica");
            p.setGlutenFree(false);
            p = repoIngr.save(p);
            result = result + p.toString() + "\n";
        }
        if (!repoIngr.findBycommonName("Base classica senza glutine").isPresent()) {
            p = new Pastry();
            p.setCommonName("Base classica senza glutine");
            p.setGlutenFree(true);
            p = repoIngr.save(p);
            result = result + p.toString() + "\n";
        }

        if (!repoIngr.findBycommonName("Base al carbone").isPresent()) {

            p = new Pastry();
            p.setCommonName("Base al carbone");
            p.setGlutenFree(true);
            p = repoIngr.save(p);
            result = result + p.toString() + "\n";
        }

        if (!repoIngr.findBycommonName("Base al lievito di birra").isPresent()) {
            p = new Pastry();
            p.setCommonName("Base al lievito di birra");
            p.setGlutenFree(false);
            p = repoIngr.save(p);
            result = result + p.toString() + "\n";
        }

        return result;
    }


    @Transactional
    public String createSeasoningDemo() {
        String result = "";
        Ingredient p = new Seasoning();
        Set<Allergen> allergen = new HashSet<Allergen>();

        if (!repoIngr.findBycommonName("Basilico").isPresent()){
            p.setCommonName("Basilico");
            allergen.add(Allergen.VERDURA);
            p.setAllergen(allergen);
            p = repoIngr.save(p);
            result = result + p.toString() + "\n";
        }

        if (!repoIngr.findBycommonName("Pomodoro").isPresent()) {
            p = new Seasoning();
            p.setCommonName("Pomodoro");
            allergen = new HashSet<Allergen>();
            allergen.add(Allergen.VERDURA);
            p.setAllergen(allergen);
            p = repoIngr.save(p);
            result = result + p.toString() + "\n";
        }

        if (!repoIngr.findBycommonName("Mozzarella").isPresent()) {
            p = new Seasoning();
            p.setCommonName("Mozzarella");
            allergen = new HashSet<Allergen>();
            allergen.add(Allergen.LATTICINI);
            p.setAllergen(allergen);
            p = repoIngr.save(p);
            result = result + p.toString() + "\n";
        }

        if (!repoIngr.findBycommonName("Wurstler").isPresent()) {
            p = new Seasoning();
            p.setCommonName("Wurstler");
            allergen = new HashSet<Allergen>();
            allergen.add(Allergen.CARNE);
            p.setAllergen(allergen);
            p = repoIngr.save(p);
            result = result + p.toString() + "\n";
        }

        if (!repoIngr.findBycommonName("Patate Fritte").isPresent()) {
            p = new Seasoning();
            p.setCommonName("Patate Fritte");
            p = repoIngr.save(p);
            result = result + p.toString() + "\n";
        }

        if (!repoIngr.findBycommonName("Prosciutto").isPresent()) {
            p = new Seasoning();
            p.setCommonName("Prosciutto");
            allergen.add(Allergen.CARNE);
            p.setAllergen(allergen);
            p = repoIngr.save(p);
            result = result + p.toString() + "\n";
        }

        if (!repoIngr.findBycommonName("Salame").isPresent()) {
            p = new Seasoning();
            p.setCommonName("Salame");
            p.setAllergen(allergen);
            p = repoIngr.save(p);
            result = result + p.toString() + "\n";
        }

        if (!repoIngr.findBycommonName("Parmigiano").isPresent()) {
            p = new Seasoning();
            p.setCommonName("Parmigiano");
            p = repoIngr.save(p);
            result = result + p.toString() + "\n";
        }

        if (!repoIngr.findBycommonName("Rucola").isPresent()) {
            p = new Seasoning();
            p.setCommonName("Rucola");
            p = repoIngr.save(p);
            result = result + p.toString() + "\n";
        }

        if (!repoIngr.findBycommonName("Pomodorini").isPresent()) {
            p = new Seasoning();
            p.setCommonName("Pomodorini");
            p = repoIngr.save(p);
            result = result + p.toString() + "\n";
        }

        if (!repoIngr.findBycommonName("Speck").isPresent()) {
            p = new Seasoning();
            p.setCommonName("Speck");
            p.setAllergen(allergen);
            p = repoIngr.save(p);
            result = result + p.toString() + "\n";
        }

        if (!repoIngr.findBycommonName("Aglio").isPresent()) {
            p = new Seasoning();
            p.setCommonName("Aglio");
            p = repoIngr.save(p);
            result = result + p.toString() + "\n";
        }

        return result;
    }

    @Transactional
    public String createPizzaDemo(){
        String result = "";
        Pizza p = new Pizza();
        Set<Seasoning> seasonings = new HashSet<Seasoning>();

        if (!repoPizza.findByCommonName("Margherita").isPresent()) {
            p.setCommonName("Margherita");
            //p.setPastry((Pastry)(repoIngr.findById(Long.parseLong("2"))).get());
            seasonings.add((Seasoning) (repoIngr.findBycommonName("Mozzarella")).get());
            seasonings.add((Seasoning) (repoIngr.findBycommonName("Pomodoro")).get());
            seasonings.add((Seasoning) (repoIngr.findBycommonName("Basilico")).get());
            p.setSeasonings(seasonings);
            p = repoPizza.save(p);
            result = result + p.toString() + "\n";
        }

        if (!repoPizza.findByCommonName("Wurstler").isPresent()) {
            p = new Pizza();
            p.setCommonName("Wurstler");
            //p.setPastry((Pastry)(repoIngr.findById(Long.parseLong("2"))).get());
            seasonings = new HashSet<Seasoning>();
            seasonings.add((Seasoning) (repoIngr.findBycommonName("Mozzarella")).get());
            seasonings.add((Seasoning) (repoIngr.findBycommonName("Pomodoro")).get());
            seasonings.add((Seasoning) (repoIngr.findBycommonName("Wurstler")).get());
            p.setSeasonings(seasonings);
            p = repoPizza.save(p);
            result = result + p.toString() + "\n";
        }

        if (!repoPizza.findByCommonName("Marinara Senz'Aglio").isPresent()) {
            p = new Pizza();
            p.setCommonName("Marinara Senz'Aglio");
            //p.setPastry((Pastry)(repoIngr.findById(Long.parseLong("2"))).get());
            seasonings = new HashSet<Seasoning>();
            seasonings.add((Seasoning) (repoIngr.findBycommonName("Pomodoro")).get());
            p.setSeasonings(seasonings);
            p = repoPizza.save(p);
            result = result + p.toString() + "\n";
        }

        if (!repoPizza.findByCommonName("Pizza Pazza").isPresent()) {
            p = new Pizza();
            p.setCommonName("Pizza Pazza");
            //p.setPastry((Pastry)(repoIngr.findById(Long.parseLong("2"))).get());
            seasonings = new HashSet<Seasoning>();
            seasonings.add((Seasoning) (repoIngr.findBycommonName("Mozzarella")).get());
            seasonings.add((Seasoning) (repoIngr.findBycommonName("Pomodoro")).get());
            seasonings.add((Seasoning) (repoIngr.findBycommonName("Wurstler")).get());
            seasonings.add((Seasoning) (repoIngr.findBycommonName("Patate Fritte")).get());
            p.setSeasonings(seasonings);
            p = repoPizza.save(p);
            result = result + p.toString() + "\n";
        }

        if (!repoPizza.findByCommonName("Pizza Squisita").isPresent()) {
            p = new Pizza();
            p.setCommonName("Pizza Squisita");
            //p.setPastry((Pastry)(repoIngr.findById(Long.parseLong("2"))).get());
            seasonings = new HashSet<Seasoning>();
            seasonings.add((Seasoning) (repoIngr.findBycommonName("Mozzarella")).get());
            seasonings.add((Seasoning) (repoIngr.findBycommonName("Pomodoro")).get());
            seasonings.add((Seasoning) (repoIngr.findBycommonName("Pomodorini")).get());
            seasonings.add((Seasoning) (repoIngr.findBycommonName("Rucola")).get());
            seasonings.add((Seasoning) (repoIngr.findBycommonName("Parmigiano")).get());
            p.setSeasonings(seasonings);
            p = repoPizza.save(p);
            result = result + p.toString() + "\n";
        }

        if (!repoPizza.findByCommonName("Pizza Salumi").isPresent()) {
            p = new Pizza();
            p.setCommonName("Pizza Salumi");
            //p.setPastry((Pastry)(repoIngr.findById(Long.parseLong("2"))).get());
            seasonings = new HashSet<Seasoning>();
            seasonings.add((Seasoning) (repoIngr.findBycommonName("Mozzarella")).get());
            seasonings.add((Seasoning) (repoIngr.findBycommonName("Pomodoro")).get());
            seasonings.add((Seasoning) (repoIngr.findBycommonName("Salame")).get());
            seasonings.add((Seasoning) (repoIngr.findBycommonName("Prosciutto")).get());
            seasonings.add((Seasoning) (repoIngr.findBycommonName("Speck")).get());
            p.setSeasonings(seasonings);
            p = repoPizza.save(p);
            result = result + p.toString() + "\n";
        }

        return result;
    }




}

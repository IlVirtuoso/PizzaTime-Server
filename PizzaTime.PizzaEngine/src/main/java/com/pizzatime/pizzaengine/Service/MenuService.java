package com.pizzatime.pizzaengine.Service;

import com.pizzatime.pizzaengine.Component.GenericResponse;
import com.pizzatime.pizzaengine.Component.Order;
import com.pizzatime.pizzaengine.Component.OrderRows;
import com.pizzatime.pizzaengine.Component.PizzeriaCostForOrder;
import com.pizzatime.pizzaengine.Controller.SearchEngineController;
import com.pizzatime.pizzaengine.Model.*;
import com.pizzatime.pizzaengine.Repository.*;
import com.pizzatime.pizzaengine.Service.amqp.IOrderExchangeService;
import com.pizzatime.pizzaengine.Service.amqp.OrderRecord;
import com.pizzatime.pizzaengine.Service.amqp.SubmissionReport;
import com.pizzatime.pizzaengine.Service.amqp.SubmissionRow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class MenuService {

    private static final int searchExpiration = 28800;

    @Autowired
    GenericService genService;

    @Autowired
    HybernatePizzaRepositoryImpl repoPizza;

    @Autowired
    HybernateIngredientRepositoryImpl repoIngr;

    @Autowired
    HybernateMenuRepositoryImpl repoMenu;

    @Autowired
    HybernateMenuRowIngredientRepositoryImpl repoMenuIngr;

    @Autowired
    HybernateMenuRowPizzaRepositoryImpl repoMenuPizza;

    public void creteMenuDemo(){

        for(long i =1; i<=4; i++) {

            Optional<Menu> opt = repoMenu.findByPizzeriaId(i);
            if (!opt.isPresent()) {
                createMenu(i);
                opt = repoMenu.findByPizzeriaId(i);
            }
            Menu target = opt.get();
            target.setPizzeriaId(i);
            target = repoMenu.save(target);
            switch ((int) i) {
                case 1:
                    this.addPizzaRow(i, "Margherita", 1, (float) 5.00);
                    this.addPizzaRow(i, "Marinara Triste", 3, (float) 3.50);
                    this.addPizzaRow(i, "Estiva", 5, (float) 7.00);
                    this.addPizzaRow(i, "Viennese", 2, (float) 5.50);
                    this.addPizzaRow(i, "Patatine e Wurstler", 4, (float) 6.00);

                    this.addIngredientRow(i, "Impasto Classico", 1, (float) 0.00);
                    this.addIngredientRow(i, "Senza Glutine", 2, (float) 0.20);

                    this.addIngredientRow(i, "Basilico", 5, (float) 0.00);
                    this.addIngredientRow(i, "Pomodoro", 6, (float) 0.30);
                    this.addIngredientRow(i, "Mozzarella", 7, (float) 0.30);
                    this.addIngredientRow(i, "Wurstler", 8, (float) 0.50);
                    this.addIngredientRow(i, "Patatine", 9, (float) 0.40);
                    this.addIngredientRow(i, "Prosciutto", 10, (float) 0.60);
                    this.addIngredientRow(i, "Parmigiano", 12, (float) 1.00);
                    this.addIngredientRow(i, "Rucola", 13, (float) 0.20);
                    this.addIngredientRow(i, "Pomodorini", 14, (float) 0.30);
                    this.addIngredientRow(i, "Aglio", 16, (float) 0.10);

                    break;
                case 2:
                    this.addPizzaRow(i, "Margherita", 1, (float) 5.00);
                    this.addPizzaRow(i, "Marinara Senza Aglio", 3, (float) 3.50);
                    this.addPizzaRow(i, "Squisita", 5, (float) 7.00);

                    this.addIngredientRow(i, "Impasto Classico", 1, (float) 0.00);
                    this.addIngredientRow(i, "Senza Glutine", 2, (float) 0.20);
                    this.addIngredientRow(i, "Carbone", 3, (float) 1.00);
                    this.addIngredientRow(i, "Lievito di Birra", 4, (float) 0.50);

                    this.addIngredientRow(i, "Basilico", 5, (float) 0.00);
                    this.addIngredientRow(i, "Pomodoro", 6, (float) 0.30);
                    this.addIngredientRow(i, "Mozzarella", 7, (float) 0.30);
                    this.addIngredientRow(i, "Patatine", 9, (float) 0.40);
                    this.addIngredientRow(i, "Parmigiano", 12, (float) 1.00);
                    this.addIngredientRow(i, "Rucola", 13, (float) 0.20);
                    this.addIngredientRow(i, "Pomodorini", 14, (float) 0.30);

                    break;
                case 3:
                    this.addPizzaRow(i, "Marghe", 1, (float) 5.00);
                    this.addPizzaRow(i, "Kiss", 3, (float) 3.50);
                    this.addPizzaRow(i, "Beatles", 5, (float) 7.00);
                    this.addPizzaRow(i, "Cannibal Corpse", 6, (float) 9.00);
                    this.addPizzaRow(i, "Crazy Pizza-Train", 4, (float) 6.50);
                    this.addPizzaRow(i, "Wind Rose", 2, (float) 5.50);

                    this.addIngredientRow(i, "Impasto Classico", 1, (float) 0.00);
                    this.addIngredientRow(i, "Senza Glutine", 2, (float) 0.20);
                    this.addIngredientRow(i, "Carbone", 3, (float) 1.00);
                    this.addIngredientRow(i, "Lievito di Birra", 4, (float) 0.50);

                    this.addIngredientRow(i, "Basilico", 5, (float) 0.00);
                    this.addIngredientRow(i, "Pomodoro", 6, (float) 0.30);
                    this.addIngredientRow(i, "Mozzarella", 7, (float) 0.30);
                    this.addIngredientRow(i, "Wurstler", 8, (float) 0.50);
                    this.addIngredientRow(i, "Patatine", 9, (float) 0.40);
                    this.addIngredientRow(i, "Prosciutto", 10, (float) 0.60);
                    this.addIngredientRow(i, "Salame", 11, (float) 0.60);
                    this.addIngredientRow(i, "Parmigiano", 12, (float) 1.00);
                    this.addIngredientRow(i, "Rucola", 13, (float) 0.20);
                    this.addIngredientRow(i, "Pomodorini", 14, (float) 0.30);
                    this.addIngredientRow(i, "Speck", 15, (float) 0.70);

                    break;
                case 4:
                    this.addPizzaRow(i, "Margherita", 1, (float) 4.50);
                    this.addPizzaRow(i, "Solo Salumi", 6, (float) 7.80);
                    this.addPizzaRow(i, "Pizza Complessa", 4, (float) 6.20);
                    this.addPizzaRow(i, "Viennese", 2, (float) 5.80);
                    this.addPizzaRow(i, "Tutta Rossa", 3, (float) 3.50);

                    this.addIngredientRow(i, "Impasto Classico", 1, (float) 0.00);

                    this.addIngredientRow(i, "Basilico", 5, (float) 0.00);
                    this.addIngredientRow(i, "Pomodoro", 6, (float) 0.30);
                    this.addIngredientRow(i, "Mozzarella", 7, (float) 0.30);
                    this.addIngredientRow(i, "Wurstler", 8, (float) 0.50);
                    this.addIngredientRow(i, "Patatine", 9, (float) 0.40);
                    this.addIngredientRow(i, "Prosciutto", 10, (float) 0.60);
                    this.addIngredientRow(i, "Salame", 11, (float) 0.60);
                    this.addIngredientRow(i, "Parmigiano", 12, (float) 1.00);
                    this.addIngredientRow(i, "Rucola", 13, (float) 0.20);
                    this.addIngredientRow(i, "Pomodorini", 14, (float) 0.30);
                    this.addIngredientRow(i, "Speck", 15, (float) 0.70);

                    break;
            }
        }
    }

    public String createMenu(long pizzeriaForMenu) {
        GenericResponse resp = new GenericResponse();

        Optional<Menu> opt = repoMenu.findByPizzeriaId(pizzeriaForMenu);

        if(opt.isPresent()){
            resp.setStatusCode(GenericResponse.ALREADY_EXISTING_ITEM_CODE);
            resp.setStatusReason(GenericResponse.ALREADY_EXISTING_ITEM_MESSAGE);
            return resp.jsonfy();
        }else {
            Menu m = new Menu();
            m.setPizzeriaId(pizzeriaForMenu);
            m = repoMenu.save(m);
            resp.setMenu(m);
            resp.setStatusCode(GenericResponse.OK_CODE);
            resp.setStatusReason(GenericResponse.OK_MESSAGE);
            return resp.jsonfy();
        }
    }

    public Boolean openPizzeria(long pizzeriaId){

        Optional<Menu> mTarget = repoMenu.findByPizzeriaId(pizzeriaId);
        if(mTarget.isPresent()){
            mTarget.get().setAvaillable(true);
            mTarget.get().setAvaillableTimestamp((System.currentTimeMillis()/1000));
            repoMenu.save(mTarget.get());
            return true;
        }
        return false;
    }


    public Boolean closePizzeria(long pizzeriaId){

        Optional<Menu> mTarget = repoMenu.findByPizzeriaId(pizzeriaId);
        if(mTarget.isPresent()){
            mTarget.get().setAvaillable(false);
            mTarget.get().setAvaillableTimestamp((long)-1);
            repoMenu.save(mTarget.get());
            return true;
        }
        return false;
    }

    public GenericResponse addPizzaRow(long pizzeriaId, String commonName, long pizzaId, float cost ){
        GenericResponse resp = new GenericResponse();

        MenuRowPizza mrp = new MenuRowPizza();
        mrp.setCommonName(commonName);
        mrp.setCost(cost);

        Optional<Pizza> targetPizza = repoPizza.findById(pizzaId);
        if(targetPizza.isPresent()){
            mrp.setPizza(targetPizza.get());
        }else{
            resp.setStatusCode(GenericResponse.INVALID_PARAMETER_CODE);
            resp.setStatusReason(GenericResponse.INVALID_PARAMETER_MESSAGE);
            return resp;
        }
        return addPizzaRow(pizzeriaId, mrp);
    }

    @Transactional
    public GenericResponse addPizzaRow(long pizzeriaId, MenuRowPizza mrp){
        GenericResponse resp = new GenericResponse();

        System.out.println("time to add a new row");
        Optional<Menu> mTarget = repoMenu.findByPizzeriaId(pizzeriaId);
        if(mTarget.isPresent() && mrp != null){
            System.out.println("I found the menu and check the row");
            if(isPizzaInMenu(mTarget.get(), mrp.getPizza()) == null) {
                System.out.println("CREATE AND ADD THE NEW ROW");
                Pizza p = genService.getOnePizzaPerfectMatch(mrp.getPizza().getSeasonings());
                mrp.setPizza(p);
                MenuRowPizza newMenuRow = repoMenuPizza.save(mrp);
                Menu m = mTarget.get();
                m.getPizzaRows().add(newMenuRow);
                m = repoMenu.save(m);
                resp.setStatusCode(GenericResponse.OK_CODE);
                resp.setStatusReason(GenericResponse.OK_MESSAGE);
                resp.setMenu(m);
                return resp;
            }else{
                System.out.println("IT IS SUFFICIENT TO MODIFY THE ROW");
                MenuRowPizza rowToEdit = isPizzaInMenu(mTarget.get(), mrp.getPizza());
                Pizza p = genService.getOnePizzaPerfectMatch(mrp.getPizza().getSeasonings());
                rowToEdit.setPizza(p);
                rowToEdit.setCommonName(mrp.getCommonName());
                rowToEdit.setCost(mrp.getCost());
                repoMenuPizza.save(rowToEdit);
                Menu m = mTarget.get();
                m = repoMenu.save(m);

                System.out.println("Edit an already Existing pizza row");
                resp.setStatusCode(GenericResponse.OK_CODE);
                resp.setStatusReason(GenericResponse.OK_MESSAGE);
                resp.setMenu(m);
                return resp;
            }
        }else{
        resp.setStatusCode(GenericResponse.NOT_EXISTING_ITEM_CODE);
        resp.setStatusReason(GenericResponse.NOT_EXISTING_ITEM_MESSAGE);
        return resp;
        }
    }

    @Transactional
    public GenericResponse addIngredientRow(long pizzeriaId, String commonName, long ingrId, float cost ) {
        GenericResponse resp = new GenericResponse();

        MenuRowIngredient mri = new MenuRowIngredient();
        mri.setCost(cost);

        Optional<Ingredient> targetIngr = repoIngr.findById(ingrId);
        if(targetIngr.isPresent()){

            mri.setCommonName(targetIngr.get().getCommonName());
            mri.setIngredient(targetIngr.get());
        }else{
            resp.setStatusCode(GenericResponse.INVALID_PARAMETER_CODE);
            resp.setStatusReason(GenericResponse.INVALID_PARAMETER_MESSAGE);
            return resp;
        }
        return addIngredientRow(pizzeriaId, mri);
    }

    @Transactional
    public GenericResponse addIngredientRow(long pizzeriaId, MenuRowIngredient mri){
        GenericResponse resp = new GenericResponse();

        Optional<Menu> mTarget = repoMenu.findByPizzeriaId(pizzeriaId);
        if(mTarget.isPresent() && mri != null){
            if(isIngrInMenu(mTarget.get(), mri.getIngredient()) == null) {
                System.out.println("CREATE AND ADD THE NEW ROW");
                MenuRowIngredient newMenuRow = repoMenuIngr.save(mri);
                Menu m = mTarget.get();
                m.getIngrRows().add(newMenuRow);
                m=repoMenu.save(m);
                resp.setStatusCode(GenericResponse.OK_CODE);
                resp.setStatusReason(GenericResponse.OK_MESSAGE);
                resp.setMenu(m);
                return resp;
            }else{
                System.out.println("IT IS SUFFICIENT TO MODIFY THE ROW");
                MenuRowIngredient rowToEdit = isIngrInMenu(mTarget.get(), mri.getIngredient());
                rowToEdit.setIngredient(mri.getIngredient());
                rowToEdit.setCommonName(mri.getCommonName());
                rowToEdit.setCost(mri.getCost());
                repoMenuIngr.save(mri);
                Menu m = mTarget.get();
                m=repoMenu.save(m);

                System.out.println("Edit an already existing ingredient row");
                resp.setStatusCode(GenericResponse.OK_CODE);
                resp.setStatusReason(GenericResponse.OK_MESSAGE);
                resp.setMenu(m);
                return resp;
            }
        }
        resp.setStatusCode(GenericResponse.INVALID_PARAMETER_CODE);
        resp.setStatusReason(GenericResponse.INVALID_PARAMETER_MESSAGE);
        return resp;
    }

    @Transactional
    public MenuRowPizza isPizzaInMenu(Menu m, Pizza p){
        Pizza target = genService.getOnePizzaPerfectMatch(p.getSeasonings());
        if(target!=null){
            Set<MenuRowPizza> mp = m.getPizzaRows();
            if(mp!=null) {
                for (MenuRowPizza checkId : mp) {
                    if (checkId.getPizza().getId() == target.getId())
                        return checkId;
                }
            }
        }
        return null;
    }


    public MenuRowIngredient isIngrInMenu(Menu m, Ingredient p){
        Optional<Ingredient> target = repoIngr.findBycommonName(p.getCommonName());
        if(target.isPresent()){
            Set<MenuRowIngredient> mi = m.getIngrRows();
            if(mi!=null) {
                for (MenuRowIngredient checkId : mi) {
                    if (checkId.getIngredient().getId() == target.get().getId())
                        return checkId;
                }
            }
        }return null;
    }

    public Set<Menu> searchMenuForPizza(long pizzaId) {
        Optional<Pizza> p = repoPizza.findById(pizzaId);
        if(p.isPresent()){
            Set<Menu> l = new HashSet<>();
            l = repoMenu.findByPizza(p.get().getId());
            if(!l.isEmpty()){
                for(Menu m : l){
                    System.out.println("Menu from Pizzeria "+m.getPizzeriaId());
                }
                return l;
            }else{
                System.out.println("No menu has this pizza");
                return null;
            }
        }
        System.out.println("This pizza doesn't exist");
        return null;
    }

    public Set<Menu> searchMenuForAddition(long additionId) {
        Optional<Ingredient> p = repoIngr.findById(additionId);
        if(p.isPresent()){
            Set<Menu> l = new  HashSet<Menu>();
            l = repoMenu.findByIngredient(p.get().getId());
            if(!l.isEmpty()){
                for(Menu m : l){
                    System.out.println("Menu from Pizzeria "+m.getPizzeriaId());
                }
                return l;
            }else{
                System.out.println("No menu has this addition");
                return null;
            }
        }
        System.out.println("This ingredient doesn't exist");
        return null;
    }

    public List<Long> searchPizzeriaForOrder(Order order) {

        ArrayList<Long> targetPizzeriaIds = new ArrayList<Long>();
        if(order!= null && !order.order.isEmpty()){

            Collection<Menu> targetMenu = new HashSet<>();
            Collection<Menu> allMenus = repoMenu.findAll();


            int i = 1;
            for(OrderRows o : order.order){
              if(o.pastryId!=null && o.pizzaId!=null) {
                  long pastryId = o.pastryId;
                  long pizzaId = o.pizzaId;

                  System.out.println("Searching Pastry for order's row "+i);
                  Collection<Menu> pastryMenu = searchMenuForAddition(pastryId);

                  if(pastryMenu==null){
                      return null;
                  }

                  System.out.println("Searching Pizza for order's row "+i);
                  Collection<Menu> pizzaMenu = searchMenuForPizza(pizzaId);

                  if(pizzaMenu==null){
                      return null;
                  }

                  allMenus.retainAll(pastryMenu);
                  allMenus.retainAll(pizzaMenu);

                  if(o.additions!=null && !o.additions.isEmpty()){
                      List<Long> additions = o.additions;
                      System.out.println("Searching additions for order's row "+i);

                      for(Long ingrId : additions){
                          Collection<Menu> additionMenu = searchMenuForAddition(ingrId);
                            if(additionMenu== null){
                                return null;
                            }
                          allMenus.retainAll(additionMenu);
                      }
                  }
              }
                i++;
            }
            if(allMenus!=null && !allMenus.isEmpty()){
                for(Menu m: allMenus){
                    targetPizzeriaIds.add(m.getPizzeriaId());
                }


                return targetPizzeriaIds;
            }
        }
        return null;
    }

    public ArrayList<PizzeriaCostForOrder> searchPizzeriaForOrderWithCost(Order order){

        List<Long> targetPizzeriaIds = searchPizzeriaForOrder(order);
        if(targetPizzeriaIds!=null && !targetPizzeriaIds.isEmpty()){
            return computeCost(order,targetPizzeriaIds);
        }else{
            return null;
        }
    }

    public Order convertOrderAMQPtoInternal(OrderRecord order){
        int fakeId = -1;
        Order o = new Order((long)fakeId, new ArrayList<OrderRows>(order.getOrderRows()));
        return o;
    }

    public ArrayList<PizzeriaCostForOrder> computeCost(Order order, List<Long> pizzeriaIds){
        ArrayList<PizzeriaCostForOrder> targetPizzeriaAndCostIds = new ArrayList<PizzeriaCostForOrder>();

        for(Long pizzeriaId : pizzeriaIds){
            float cost = 0;

            System.out.println("start to compute the cost for pizzeria "+pizzeriaId);
            Optional<Menu> opt = repoMenu.findByPizzeriaId(pizzeriaId);
            // CHECK ON AVAILLABILITY
            if(opt.isPresent() && (opt.get().getAvaillable()!=null && opt.get().getAvaillable()) &&
                    (opt.get().getAvaillableTimestamp()!=null && opt.get().getAvaillableTimestamp()+searchExpiration > ((System.currentTimeMillis() / 1000)))) {
                for (OrderRows o : order.order) {
                    int i = 1;
                    if (o.pastryId != null && o.pizzaId != null) {
                        long pastryId = o.pastryId;
                        long pizzaId = o.pizzaId;
                        System.out.println("Searching Pastry'cost for order's row " + i + " and pizzeria " + pizzeriaId);
                        HashSet<MenuRowIngredient> pastryRow = repoMenu.findRowByIngredient(pastryId, pizzeriaId);
                        if (pastryRow == null || pastryRow.isEmpty() || pastryRow.size() > 1) {
                            System.out.println("Failed to find a pastry for cost computation");
                            return null;
                        } else {
                            for (MenuRowIngredient mri : pastryRow) {
                                cost = cost + mri.getCost();
                            }
                        }
                        System.out.println("Searching Pizza's cost for order's row " + i + " and pizzeria " + pizzeriaId);
                        HashSet<MenuRowPizza> pizzaRow = repoMenu.findRowByPizza(pizzaId, pizzeriaId);
                        if (pizzaRow == null || pizzaRow.isEmpty() || pizzaRow.size() > 1) {
                            System.out.println("Failed to find a pizza for cost computation");
                            return null;
                        } else {
                            for (MenuRowPizza mrp : pizzaRow) {
                                cost = cost + mrp.getCost();
                            }
                        }
                        if (o.additions != null && !o.additions.isEmpty()) {
                            List<Long> additions = o.additions;
                            System.out.println("Searching additions' cost for order's row " + i + " and pizzeria " + pizzeriaId);

                            for (Long ingrId : additions) {
                                HashSet<MenuRowIngredient> additionRow = repoMenu.findRowByIngredient(ingrId, pizzeriaId);

                                if (additionRow == null || additionRow.isEmpty() || additionRow.size() > 1) {
                                    System.out.println("Failed to find an ingredient for cost computation");
                                    return null;
                                } else {
                                    for (MenuRowIngredient mri : additionRow) {
                                        cost = cost + mri.getCost();
                                    }
                                }

                            }
                        }
                    }
                    i++;
                }
                PizzeriaCostForOrder data = new PizzeriaCostForOrder(pizzeriaId, cost);
                targetPizzeriaAndCostIds.add(data);
            }else{
                System.out.println("Unfortunately pizzeria "+pizzeriaId+" is closed");
                System.out.println("Is pizzeria open? " + opt.get().getAvaillable() );
                System.out.println("pizzeria opens up at  " + opt.get().getAvaillableTimestamp() +" and now it is "+ (System.currentTimeMillis() / 1000));
            }
        }
        return targetPizzeriaAndCostIds;
    }

    public List<Menu> getMenuForOrder(Order order){
        List<Menu> targetMenus = new ArrayList<>();

        if(order.pizzeriaId!=null && order.pizzeriaId>=0 && order.order!=null && !order.order.isEmpty()){
            long pizzeriaId = order.pizzeriaId;
            Optional<Menu> optMenu = repoMenu.findByPizzeriaId(pizzeriaId);
            if(optMenu.isPresent()){
                long i = 0;
                for(OrderRows o : order.order){
                    if(o.pastryId!=null && o.pizzaId!=null) {
                        long pastryId = o.pastryId;
                        long pizzaId = o.pizzaId;
                        Menu m = new Menu();
                        if(o.additions!=null && !o.additions.isEmpty()){
                            System.out.println("search also for addditional ingredients");
                            m = searchMenuRowForOrder(pastryId,pizzaId,o.additions,pizzeriaId);
                        }else{ m = searchMenuRowForOrder(pastryId,pizzaId,pizzeriaId); }

                        if(m!=null){
                            m.setId(i);
                            m.setPizzeriaId(pizzeriaId);
                            targetMenus.add(m);
                            i++;
                        }else{
                            System.out.println("Beacause of some missing item the service failed");
                            return null;
                        }
                    }
                }
            }
        }
        return targetMenus;
    }













    /** INTERNAL USE */

    public Menu searchMenuRowForOrder(long pastryId, long pizzaId, long pizzeriaId) {
        return searchMenuRowForOrder(pastryId,pizzaId,null,pizzeriaId);
    }

    public Menu searchMenuRowForOrder(long pastryId, long pizzaId, List<Long> additions, long pizzeriaId) {

        Menu newMenu = new Menu();
        Set<MenuRowIngredient> additionRow = new HashSet<>();
        Set<MenuRowPizza> pizzaRow = new HashSet<>();
        Set<MenuRowIngredient> support = new HashSet<>();

        Optional<Ingredient> pastry = repoIngr.findById(pastryId);
        if(pastry.isPresent()){
            additionRow = (HashSet)repoMenu.findRowByIngredient(pastry.get().getId(), pizzeriaId);
            if(additionRow.isEmpty()){
                System.out.println("PastryId "+pastryId+" doesn't exist for pizzeria "+pizzeriaId);
                return null;
            }

        }else {
            System.out.println("PastryId "+pastryId+" doesn't exist");
            return null;
        }

        Optional<Pizza> pizza = repoPizza.findById(pizzaId);
        if(pizza.isPresent()){
            pizzaRow = (HashSet)repoMenu.findRowByPizza(pizza.get().getId(), pizzeriaId);
            if(pizzaRow.isEmpty()){
                System.out.println("PizzaId "+pizzaId+" doesn't exist for pizzeria "+pizzeriaId);
                return null;
            }
        }else{
            System.out.println("PizzaId "+pizzaId+" doesn't exist");
            return null;
        }

        if(additions!=null && !additions.isEmpty()){
            for(Long l : additions){
                Optional<Ingredient> add = repoIngr.findById(l);
                if(add.isPresent()){
                    support = (HashSet)repoMenu.findRowByIngredient(add.get().getId(), pizzeriaId);
                    if(support.isEmpty()){
                        System.out.println("AdditionId "+l+" doesn't exist for pizzeria "+pizzeriaId);
                        return null;
                    }else{
                        additionRow.addAll(support);
                        support.clear();
                    }

                }else {
                    System.out.println("AdditionId "+l+" doesn't exist");
                    return null;
                }
            }
        }
        newMenu.setIngrRows((Set)additionRow);
        newMenu.setPizzaRows((Set)pizzaRow);

        return newMenu;
    }



    public Menu searchMenuRowForPizza(long pizzaId, long pizzeriaId) {
        Optional<Pizza> p = repoPizza.findById(pizzaId);
        if(p.isPresent()){
            Set<MenuRowPizza> l = new HashSet<>();
            Menu newMenu = new Menu();
            l = (HashSet)repoMenu.findRowByPizza(p.get().getId(), pizzeriaId);
            newMenu.setPizzaRows((Set)l);
            return newMenu;
        }
        return null;
    }

    public Menu searchMenuRowForIngredient(long additionId, long pizzeriaId) {
        Optional<Ingredient> p = repoIngr.findById(additionId);
        if(p.isPresent()){
            Set<MenuRowIngredient> l = new HashSet<>();
            Menu newMenu = new Menu();
            l = (HashSet)repoMenu.findRowByIngredient(p.get().getId(), pizzeriaId);
            newMenu.setIngrRows((Set)l);
            return newMenu;
        }
        return null;
    }


    /** DEBUG METHOD */

    public void debugSearchMenuForPizza(long pizzaId) {
        Optional<Pizza> p = repoPizza.findById(pizzaId);
        if(p.isPresent()){
            Set<Menu> l = new HashSet<>();
            l = repoMenu.findByPizza(p.get().getId());
            if(!l.isEmpty()){
                for(Menu m : l){
                    System.out.println("Menu from Pizzeria "+m.getPizzeriaId());
                }
            }else{
                System.out.println("No menu has this pizza");
            }
        }
    }

    public void debugSearchMenuForAddition(long additionId) {
        Optional<Ingredient> p = repoIngr.findById(additionId);
        if(p.isPresent()){
            Set<Menu> l = new HashSet<>();
            l = repoMenu.findByIngredient(p.get().getId());
            if(!l.isEmpty()){
                for(Menu m : l){
                    System.out.println("Menu from Pizzeria "+m.getPizzeriaId());
                }
            }else{
                System.out.println("No menu has this addition");
            }
        }
    }

    /** Questi metodi li userò uno di seguito all'altro per formattare l'info a FE
     * Posso usare il campo ID del nuovo oggetto Menù per mettere il numero di righe progressivo*/

    public Menu debugSearchMenuRowForPizza(long pizzaId, long pizzeriaId) {
        Optional<Pizza> p = repoPizza.findById(pizzaId);
        if(p.isPresent()){
            Set<MenuRowPizza> l = new HashSet<>();
            Menu newMenu = new Menu();
            l = (HashSet)repoMenu.findRowByPizza(p.get().getId(), pizzeriaId);
            newMenu.setPizzaRows((Set)l);
            return newMenu;
        }
        return null;
    }

    public Menu debugSearchMenuRowForIngredient(long additionId, long pizzeriaId) {
        Optional<Ingredient> p = repoIngr.findById(additionId);
        if(p.isPresent()){
            Set<MenuRowIngredient> l = new HashSet<>();
            Menu newMenu = new Menu();
            l = (HashSet)repoMenu.findRowByIngredient(p.get().getId(), pizzeriaId);
            newMenu.setIngrRows((Set)l);
            return newMenu;
        }
        return null;
    }


}

package com.pizzatime.pizzaengine.Controller;

import com.google.gson.Gson;
import com.pizzatime.pizzaengine.Component.GenericResponse;
import com.pizzatime.pizzaengine.Model.*;
import com.pizzatime.pizzaengine.Service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/item/v1")
public class itemController {




    public static final boolean debug = true;

    @Autowired
    IUserAuthorizationService userAuthorizationService;

    @Autowired
    PizzaEngineService searchUtilities;

    @Autowired
    PizzaService pizzaService;

    @Autowired
    MenuService menuService;

    @Autowired
    GenericService genService;

    /** Inner class per la Request della changePassword*/
    public static class createPizzeriaRequest{
        public ArrayList<Long> ingredient;
        public String commonName;
    }

    public class AddPizzaRequestComponent{
        public Float cost;
        public String commonName;
        public ArrayList<Long> ingredients;
    }

    public class AddPizzaRequest{
        public Long pizzeriaId;
        public ArrayList<AddPizzaRequestComponent> pizzas;
    }

    public class AddIngredientRequestComponent{
        public Float cost;
        public String commonName;
        public Long addition;
    }

    public class AddIngredientRequest{
        public Long pizzeriaId;
        public ArrayList<AddIngredientRequestComponent> additions;
    }

    /**
     * API di creazione della pizza
     * @param request
     * @return
     */
    @PostMapping("/createPizza")
    public String createPizza(@RequestBody() createPizzeriaRequest request) {
        GenericResponse resp = new GenericResponse();
        if(!request.ingredient.isEmpty() && !request.commonName.isEmpty()){
            return pizzaService.createPizza(request.commonName, request.ingredient);
        }
        else {
            resp.setCustomObj("Nothing to say to you!");
            resp.setStatusCode(GenericResponse.INVALID_PARAMETER_CODE);
            resp.setStatusReason(GenericResponse.INVALID_PARAMETER_MESSAGE);
            return resp.jsonfy();
        }
    }

    /**
     * Delete pizza
     * @param pizzaId
     * @return
     */
    @GetMapping("/deletePizza")
    public String deletePizza(@RequestParam(name="pizzaId") Long pizzaId){
        return pizzaService.deletePizza(pizzaId);
    }

    @GetMapping("/createMenu")
    public String createMenu(@RequestHeader(value = "Authorization", required = false) String sessionToken,
                             @RequestParam(value="pizzeriaId") Long pizzeriaId) {
        //CALL THE VALIDATION OF THE JWT TO EXTRACT THE PIZZERIA ID

        GenericResponse resp = new GenericResponse();

        if (debug && pizzeriaId != null) {
            long pizzeriaForMenu = pizzeriaId;
            return menuService.createMenu(pizzeriaForMenu);
        }

        resp.setStatusCode(GenericResponse.FAILED_AUTHENTICATION_CODE);
        resp.setStatusReason(GenericResponse.FAILED_AUTHENTICATION_MESSAGE);
        return resp.jsonfy();
    }

    @PostMapping("/addPizzaToMenu")
    public String addPizzaToMenu(@RequestHeader(value = "Authorization", required = false) String sessionToken,
                                 @RequestBody() String json){
        long id =-1;
        Optional<ManagerAccount> cose =  userAuthorizationService.validateManagerIdToken(sessionToken);
        Gson gson = new Gson();
        AddPizzaRequest pizzas = gson.fromJson(json, AddPizzaRequest.class);

        GenericResponse resp = new GenericResponse();
        resp.setStatusCode(GenericResponse.INVALID_PARAMETER_CODE);
        resp.setStatusReason(GenericResponse.INVALID_PARAMETER_MESSAGE);
        //CALL THE VALIDATION OF THE JWT TO EXTRACT THE PIZZERIA ID
        if(debug){
            id = pizzas.pizzeriaId;
        }

        if(pizzas !=null && pizzas.pizzas!=null && !pizzas.pizzas.isEmpty()){
            for(AddPizzaRequestComponent rc: pizzas.pizzas){
                System.out.println(rc.commonName);
                //System.out.println(rc.ingredients);
                //System.out.println(rc.cost);
                MenuRowPizza r = new MenuRowPizza();
                r.setCost(rc.cost);
                r.setCommonName(rc.commonName);
                Pizza p = new Pizza();
                ArrayList<Long> a = rc.ingredients;
                Set<Seasoning> newSet = new HashSet<Seasoning>();
                for(Long ida : a){
                    Ingredient i = genService.getIngredientInfoInternal(ida);
                    if(i!=null){newSet.add((Seasoning) i);}
                    else{System.out.println("this ingredient doesn't exist");
                        return resp.jsonfy();}
                }
                p.setSeasonings(newSet);
                r.setPizza(p);
                resp = menuService.addPizzaRow(id,r);
                if(resp.getStatusCode()!=0){
                    break;
                }
            }
        }
        return resp.jsonfy();
    }

    @PostMapping("/addAdditionToMenu")
    public String addAdditionToMenu(@RequestHeader(value = "Authorization", required = false) String sessionToken,
                                 @RequestBody() String json ){
        long id =-1;
        GenericResponse resp = new GenericResponse();
        resp.setStatusCode(GenericResponse.INVALID_PARAMETER_CODE);
        resp.setStatusReason(GenericResponse.INVALID_PARAMETER_MESSAGE);
        //CALL THE VALIDATION OF THE JWT TO EXTRACT THE PIZZERIA ID

        Gson gson = new Gson();
        AddIngredientRequest row = gson.fromJson(json, AddIngredientRequest.class);

        if(debug){
            id = row.pizzeriaId;
        }

        if(row!=null && row.additions !=null && !row.additions.isEmpty()){
            for(AddIngredientRequestComponent ing: row.additions){
                MenuRowIngredient r = new MenuRowIngredient();
                r.setCost(ing.cost);
                r.setCommonName(ing.commonName);
                Ingredient i = genService.getIngredientInfoInternal(ing.addition);
                if(i!=null) {
                    r.setIngredient(i);
                }else{
                    System.out.println("this addition doesn't exist");
                    return resp.jsonfy();
                }
                resp = menuService.addIngredientRow(id,r);
                if(resp.getStatusCode()!=0){
                    break;
                }
            }
        }
        return resp.jsonfy();
    }

    @GetMapping("/getMenu")
    public String addPizzaToMenu(@RequestHeader(value = "Authorization", required = false) String sessionToken,
                                 @RequestParam(name="pizzeriaId") long pizzeriaId) {

        // You should validate JWT before

        GenericResponse resp = new GenericResponse();
        Menu target = genService.getMenuFromPizzeriaInfoInternal(pizzeriaId);

        if(target!=null){

            resp.setStatusCode(GenericResponse.OK_CODE);
            resp.setStatusReason(GenericResponse.OK_MESSAGE);
            resp.setMenu(target);
            return resp.jsonfy();
        }

        resp.setStatusCode(GenericResponse.INVALID_PARAMETER_CODE);
        resp.setStatusReason(GenericResponse.INVALID_PARAMETER_MESSAGE);
        return resp.jsonfy();
    }

}

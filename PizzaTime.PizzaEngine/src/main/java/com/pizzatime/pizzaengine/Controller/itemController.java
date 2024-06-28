package com.pizzatime.pizzaengine.Controller;

import com.google.gson.Gson;
import com.pizzatime.pizzaengine.Component.GenericResponse;
import com.pizzatime.pizzaengine.Model.*;
import com.pizzatime.pizzaengine.Service.*;
import com.pizzatime.pizzaengine.Service.amqp.IUserAuthorizationService;
import com.pizzatime.pizzaengine.Service.amqp.ManagerAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/item/v1")
public class itemController {




    public static final boolean debug = false;

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

    @Autowired
    IUserAuthorizationService authService;


    /** Inner class per la Request della createPizza*/
    public static class createPizzaRequest{
        public ArrayList<Long> ingredient;
        public String commonName;
    }

    public class AddPizzaByIngrRequestComponent{
        public Float cost;
        public String commonName;
        public ArrayList<Long> ingredients;
    }

    public class AddPizzaByIngrRequest{
        public Long pizzeriaId;
        public ArrayList<AddPizzaByIngrRequestComponent> pizzas;
    }

    public class AddPizzaRequestComponent{
        public Float cost;
        public String commonName;
        public Long pizzaId;
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
     * @param request un oggetto JSON contenente un array di ingredienti nel campo "ingredients" ed un common name
     * @return OK se la pizza è stata integrata con successo
     */
    @PostMapping("/createPizza")
    public String createPizza(@RequestBody() createPizzaRequest request) {
        GenericResponse resp = new GenericResponse();
        if(!request.ingredient.isEmpty() && !request.commonName.isEmpty()){
            return pizzaService.createPizza(request.commonName, request.ingredient);
        }
        else {
            resp.setStatusCode(GenericResponse.INVALID_PARAMETER_CODE);
            resp.setStatusReason(GenericResponse.INVALID_PARAMETER_MESSAGE);
            return resp.jsonfy();
        }
    }

    /**
     * API di cancellazione di una pizza
     * @param pizzaId
     * @return statusCode a 0 se la pizza è stata cancellata
     */
    @GetMapping("/deletePizza")
    public String deletePizza(@RequestParam(name="pizzaId") Long pizzaId){
        return pizzaService.deletePizza(pizzaId);
    }

    /**
     * API di creazione del menu di una pizzeria
     * @param idToken nell'header di un utente vendor da cui si estrae il pizzeriaId
     * @param pizzeriaId solo in modalità debug
     * @return OK se il menù è stato creato per la pizzeria
     */
    @GetMapping("/createMenu")
    public String createMenu(@RequestHeader(value = "Authorization", required = false) String idToken,
                             @RequestParam(value="pizzeriaId", required = false) Long pizzeriaId) {
        //CALL THE VALIDATION OF THE JWT TO EXTRACT THE PIZZERIA ID

        GenericResponse resp = new GenericResponse();

        Optional<ManagerAccount> mng = authService.validateManagerIdToken(idToken);
        if (mng.isPresent()) {
            ManagerAccount manager = mng.get();

            if(!debug && (pizzeriaId == null || pizzeriaId != (Long) manager.getPizzeria().getId())) {
                pizzeriaId = (Long) manager.getPizzeria().getId();
            }

            System.out.println("Received a request for creating a menu for pizzeria" + pizzeriaId);

            return menuService.createMenu(pizzeriaId);
        }

        resp.setStatusCode(GenericResponse.FAILED_AUTHENTICATION_CODE);
        resp.setStatusReason(GenericResponse.FAILED_AUTHENTICATION_MESSAGE);
        return resp.jsonfy();
    }

    /**
     * API di aggiunta di una pizza sconosciuta, indicata cioè per ingredienti, ad un menù
     * @param idToken nell'header di un utente vendor da cui si estrae il pizzeriaId
     * @param json contenente un array pizzas in cui ogni elemento è composto da "cost":Float, "commonName":String e "ingredients":array di long
     * @return statusCode 0 se la pizza è stata trovata ed aggiunta al menù
     */
    @PostMapping("/addPizzaByIngredientsToMenu")
    public String addPizzaByIngredientsToMenu(@RequestHeader(value = "Authorization", required = false) String idToken,
                                 @RequestBody() String json) {
        long id = -1;
        Gson gson = new Gson();
        AddPizzaByIngrRequest pizzas = gson.fromJson(json, AddPizzaByIngrRequest.class);

        GenericResponse resp = new GenericResponse();
        resp.setStatusCode(GenericResponse.INVALID_PARAMETER_CODE);
        resp.setStatusReason(GenericResponse.INVALID_PARAMETER_MESSAGE);

        //CALL THE VALIDATION OF THE JWT TO EXTRACT THE PIZZERIA ID
        if (debug){
            id = pizzas.pizzeriaId;
        }else if(idToken!=null && !idToken.isEmpty()) {
            Optional<ManagerAccount> mng = authService.validateManagerIdToken(idToken);
            if (mng.isPresent()) {
                System.out.println("Authorized manager is present");
                ManagerAccount manager = mng.get();
                id = manager.getPizzeria().getId();
            }else{
                resp.setStatusCode(GenericResponse.FAILED_AUTHENTICATION_CODE);
                resp.setStatusReason(GenericResponse.FAILED_AUTHENTICATION_MESSAGE);
                return resp.jsonfy();
            }
        }else{
            resp.setStatusCode(GenericResponse.FAILED_AUTHENTICATION_CODE);
            resp.setStatusReason(GenericResponse.FAILED_AUTHENTICATION_MESSAGE);
            return resp.jsonfy();
        }

        if(pizzas !=null && pizzas.pizzas!=null && !pizzas.pizzas.isEmpty()){
            for(AddPizzaByIngrRequestComponent rc: pizzas.pizzas){
                //System.out.println(rc.ingredients);
                //System.out.println(rc.cost);
                MenuRowPizza r = new MenuRowPizza();
                r.setCost((float)rc.cost);
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

                if(rc.commonName!=null){
                    System.out.println(rc.commonName);
                    r.setCommonName(rc.commonName);
                }else{
                    r.setCommonName(p.getCommonName());
                }

                resp = menuService.addPizzaRow(id,r);
                if(resp.getStatusCode()!=0){
                    break;
                }
            }
        }
        return resp.jsonfy();
    }

    /**
     * API di aggiunta di una pizza indicata direttamente (per ID) ad un menù
     * @param idToken nell'header di un utente vendor da cui si estrae il pizzeriaId
     * @param json contenente un array pizzas in cui ogni elemento è composto da "cost":Float, "commonName":String e "pizzaId":long
     * @return statusCode 0 se la pizza è stata trovata ed aggiunta al menù
     */
    @PostMapping("/addPizzaToMenu")
    public String addPizzaToMenu(@RequestHeader(value = "Authorization", required = false) String idToken,
                                              @RequestBody() String json){
        long id =-1;
        Gson gson = new Gson();
        AddPizzaRequest pizzas = gson.fromJson(json, AddPizzaRequest.class);

        GenericResponse resp = new GenericResponse();
        resp.setStatusCode(GenericResponse.INVALID_PARAMETER_CODE);
        resp.setStatusReason(GenericResponse.INVALID_PARAMETER_MESSAGE);
        //CALL THE VALIDATION OF THE JWT TO EXTRACT THE PIZZERIA ID
        if (debug){
            id = pizzas.pizzeriaId;
        }else if(idToken!=null && !idToken.isEmpty()) {
            Optional<ManagerAccount> mng = authService.validateManagerIdToken(idToken);
            if (mng.isPresent()) {
                System.out.println("Authorized manager is present");
                ManagerAccount manager = mng.get();
                id = manager.getPizzeria().getId();
            }else{
                resp.setStatusCode(GenericResponse.FAILED_AUTHENTICATION_CODE);
                resp.setStatusReason(GenericResponse.FAILED_AUTHENTICATION_MESSAGE);
                return resp.jsonfy();
            }
        }else{
            resp.setStatusCode(GenericResponse.FAILED_AUTHENTICATION_CODE);
            resp.setStatusReason(GenericResponse.FAILED_AUTHENTICATION_MESSAGE);
            return resp.jsonfy();
        }


        if(pizzas !=null && pizzas.pizzas!=null && !pizzas.pizzas.isEmpty()){
            for(AddPizzaRequestComponent rc: pizzas.pizzas){

                MenuRowPizza r = new MenuRowPizza();
                r.setCost((float)rc.cost);

                Pizza p = new Pizza();
                Long pizzaId = rc.pizzaId;
                p = genService.getPizzaInfoInternal(pizzaId);
                if(p==null){
                    System.out.println("this pizza doesn't exist");
                    return resp.jsonfy();
                }
                r.setPizza(p);
                if(rc.commonName!=null){
                    System.out.println(rc.commonName);
                    r.setCommonName(rc.commonName);
                }else{
                    r.setCommonName(p.getCommonName());
                }
                resp = menuService.addPizzaRow(id,r);
                if(resp.getStatusCode()!=0){
                    break;
                }
            }
        }
        return resp.jsonfy();
    }

    /**
     * API di aggiunta di un ingrediente (base o condimento) indicato per ID  d un menù
     * @param idToken nell'header di un utente vendor da cui si estrae il pizzeriaId
     * @param json contenente un array pizzas in cui ogni elemento è composto da "cost":Float, "commonName":String e "addition":long
     * @return statusCode 0 se l'ingrediente è stata trovata ed aggiunta al menù
     */
    @PostMapping("/addAdditionToMenu")
    public String addAdditionToMenu(@RequestHeader(value = "Authorization", required = false) String idToken,
                                 @RequestBody() String json ){
        long id =-1;
        GenericResponse resp = new GenericResponse();
        resp.setStatusCode(GenericResponse.INVALID_PARAMETER_CODE);
        resp.setStatusReason(GenericResponse.INVALID_PARAMETER_MESSAGE);
        //CALL THE VALIDATION OF THE JWT TO EXTRACT THE PIZZERIA ID



        Gson gson = new Gson();
        AddIngredientRequest row = gson.fromJson(json, AddIngredientRequest.class);

        if (debug){
            id = row.pizzeriaId;
        }else if(idToken!=null && !idToken.isEmpty()) {
            Optional<ManagerAccount> mng = authService.validateManagerIdToken(idToken);
            if (mng.isPresent()) {
                System.out.println("Authorized manager is present");
                ManagerAccount manager = mng.get();
                id = manager.getPizzeria().getId();
            }else{
                resp.setStatusCode(GenericResponse.FAILED_AUTHENTICATION_CODE);
                resp.setStatusReason(GenericResponse.FAILED_AUTHENTICATION_MESSAGE);
                return resp.jsonfy();
            }
        }else{
            resp.setStatusCode(GenericResponse.FAILED_AUTHENTICATION_CODE);
            resp.setStatusReason(GenericResponse.FAILED_AUTHENTICATION_MESSAGE);
            return resp.jsonfy();
        }


        if(row!=null && row.additions !=null && !row.additions.isEmpty()){
            for(AddIngredientRequestComponent ing: row.additions){
                MenuRowIngredient r = new MenuRowIngredient();
                r.setCost((float)ing.cost);
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

    /**
     * Richiede il menù di una certa pizzeria
     * @param idToken nell'header di un utente vendor da cui si estrae il pizzeriaId
     * @param pizzeriaId solo in modalità debug
     * @return
     */
    @GetMapping("/getMenu")
    public String addPizzaToMenu(@RequestHeader(value = "Authorization", required = false) String idToken,
                                 @RequestParam(name="pizzeriaId", required = false) Long pizzeriaId) {

        // You should validate JWT before

        long id =-1;
        GenericResponse resp = new GenericResponse();

        if (debug && pizzeriaId!=null){
            id = pizzeriaId;
        }else if(idToken!=null && !idToken.isEmpty()) {
            Optional<ManagerAccount> mng = authService.validateManagerIdToken(idToken);
            if (mng.isPresent()) {
                System.out.println("Authorized manager is present");
                ManagerAccount manager = mng.get();
                id = manager.getPizzeria().getId();
            }else{
                resp.setStatusCode(GenericResponse.FAILED_AUTHENTICATION_CODE);
                resp.setStatusReason(GenericResponse.FAILED_AUTHENTICATION_MESSAGE);
                return resp.jsonfy();
            }
        }else{
            resp.setStatusCode(GenericResponse.FAILED_AUTHENTICATION_CODE);
            resp.setStatusReason(GenericResponse.FAILED_AUTHENTICATION_MESSAGE);
            return resp.jsonfy();
        }

        Menu target = genService.getMenuFromPizzeriaInfoInternal(id);

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

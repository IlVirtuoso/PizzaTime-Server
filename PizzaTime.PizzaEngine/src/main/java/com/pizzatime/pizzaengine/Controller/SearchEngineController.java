package com.pizzatime.pizzaengine.Controller;


import com.google.gson.Gson;
import com.pizzatime.pizzaengine.Component.GenericResponse;
import com.pizzatime.pizzaengine.Component.Order;
import com.pizzatime.pizzaengine.Component.PizzeriaCostForOrder;
import com.pizzatime.pizzaengine.Model.Menu;
import com.pizzatime.pizzaengine.Service.*;
import com.pizzatime.pizzaengine.Service.amqp.IUserAuthorizationService;
import com.pizzatime.pizzaengine.Service.amqp.ManagerAccount;
import org.apache.catalina.Manager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/search/v1")
public class SearchEngineController {

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

    public static final boolean debug = false;





    /**
     * API che restituisce tutte le pizze a catalogo
     * @return un JSON con uno status code a 0 e un campo array di nome "pizzas" con l'elenco di tutti gli oggetti Pizza a DB
     */
    @GetMapping("/getAllPizza")
    public String getAllPizza(){
        return pizzaService.getAllPizzas();
    }


    /**
     * API che restituisce tutte le basi/impasti a catalogo
     * @return un JSON con uno status code a 0 e un campo array di nome "pastries" con l'elenco di tutti gli oggetti Pastry a DB
     */
    @GetMapping("/getAllPastry")
    public String getAllPastry(){
        return pizzaService.getAllPastry();
    }


    /**
     * API che restituisce tutti i condimenti a catalogo
     * @return un JSON con uno status code a 0 e un campo array di nome "seasonings" con l'elenco di tutti gli oggetti Seasoning a DB
     */
    @GetMapping("/getAllSeasoning")
    public String getAllSeasoning(){
        return pizzaService.getAllSeasoning();
    }


    /**
     * PREVALENTEMENTE AD USO INTERNO/TEST
     * API che restituisce tutti gli ingredienti (Pastry+Seasoning) a catalogo
     * @return un JSON con uno status code a 0 e un campo array di nome "ingredients" con l'elenco di tutti gli oggetti Ingredient a DB
     */
    @GetMapping("/getAllIngredient")
    public String getAllIngredient(){
        return pizzaService.getAllIngredient();
    }


    /**
     * API che dato un ordine restituisce le pizzerie che lo possono soddisfare
     * @param json contenente un oggetto "order",ossia un array di triplette "pastryId":long, "pizzaId":long e "additions":array<long>. Quest'utlimo è opzionale
     * @return un json con all'interno un array di coppie "pizzeriaId":long e "cost":float
     *
     */
    @PostMapping("searchOnlyPizzeriaForOrder")
    public List<Long> searchOnlyPizzeriaForOrder(@RequestBody() String json){
        Gson gson = new Gson();
        Order order = gson.fromJson(json, Order.class);
        return menuService.searchPizzeriaForOrder(order);
    }


    /**
     * API che dato un ordine restituisce le pizzerie ed il conto che lo possono soddisfare
     * @param json contenente un oggetto "order",ossia un array di triplette "pastryId":long, "pizzaId":long e "additions":array<long>. Quest'utlimo è opzionale
     * @return un json con all'interno un array di coppie "pizzeriaId":long e "cost":float
     */
    @PostMapping("searchPizzeriaForOrder")
    public List<PizzeriaCostForOrder> searchPizzeriaForOrder(@RequestBody() String json){
        Gson gson = new Gson();
        Order order = gson.fromJson(json, Order.class);
        return menuService.searchPizzeriaForOrderWithCost(order);
    }

    /**
     * API ad uso del FE che dato un ordine fatto ad una pizzeria ritorna le entry del menù corrispondenti
     * @param json contenente un oggetto "order",ossia un array di triplette "pastryId":long, "pizzaId":long e "additions":array<long>. Quest'utlimo è opzionale
     * @return un array contenente i dettagli dell'ordine richiesto
     */
    @PostMapping("getMenuForOrder")
    public String getMenuForOrder(@RequestHeader(value = "Authorization", required = false) String idToken,
            @RequestBody() String json) {

        GenericResponse resp = new GenericResponse();

        Gson gson = new Gson();
        Order order = gson.fromJson(json, Order.class);

        Optional<ManagerAccount> mng = authService.validateManagerIdToken(idToken);
        if (mng.isPresent()) {
            ManagerAccount manager = mng.get();

            if(!debug && (order.pizzeriaId == null || order.pizzeriaId != (Long) manager.getPizzeria().getId())) {
                order.pizzeriaId = (Long) manager.getPizzeria().getId();
            }

            Menu m = genService.getMenuFromPizzeriaInfoInternal(order.pizzeriaId);

            System.out.println("Received a request for pizzeria" + (Long) manager.getPizzeria().getId() + " with menu:\n" + m.toString());
            List<Menu> menu= menuService.getMenuForOrder(order);
            if(menu!=null && !menu.isEmpty()){
                resp.setStatusCode(GenericResponse.OK_CODE);
                resp.setStatusReason(GenericResponse.OK_MESSAGE);
                resp.setOrderData(menu);
                return resp.jsonfy();
            }else {
                resp.setStatusCode(GenericResponse.INVALID_PARAMETER_CODE);
                resp.setStatusReason(GenericResponse.INVALID_PARAMETER_MESSAGE);
                resp.setOrderData(menu);
                return resp.jsonfy();
            }
        }

        resp.setStatusCode(GenericResponse.FAILED_AUTHENTICATION_CODE);
        resp.setStatusReason(GenericResponse.FAILED_AUTHENTICATION_MESSAGE);
        return resp.jsonfy();
    }

    /** INTERNAL USE */

    @GetMapping("/searchMenuForPizza")
    public Set<Menu> searchMenuForPizza(@RequestParam(name="pizzaId") long pizzaId){
        return menuService.searchMenuForPizza(pizzaId);
    }

    @GetMapping("/searchMenuForAddition")
    public Set<Menu> searchMenuForAddition(@RequestParam(name="additionId") long additionId){
        return menuService.searchMenuForAddition(additionId);
    }



    /** DEBUG METHODS*/

    /** Inner class per la Request della changePassword*/
    public static class HelloWorldRequest{
        public Boolean print;
    }

    @PostMapping("/postHelloWorld")
    public String postHelloWorld(@RequestBody() HelloWorldRequest print) {
        GenericResponse resp = new GenericResponse();
        if(print.print){
            resp.setStatusCode(GenericResponse.OK_CODE);
            resp.setStatusReason(GenericResponse.OK_MESSAGE);
            resp.setCustomObj("You post an Helloworld");
            return resp.jsonfy();
        }
        else {
            resp.setCustomObj("Nothing to say to you!");
            resp.setStatusCode(GenericResponse.INVALID_PARAMETER_CODE);
            resp.setStatusReason(GenericResponse.INVALID_PARAMETER_MESSAGE);
            return resp.jsonfy();
        }
    }

    @GetMapping("/getHelloWorld")
    public String getHelloWorld(@RequestParam(name="print") Boolean print) {
        GenericResponse resp = new GenericResponse();
        if(print){
            resp.setStatusCode(GenericResponse.OK_CODE);
            resp.setStatusReason(GenericResponse.OK_MESSAGE);
            resp.setCustomObj("You get an Helloworld");
            return resp.jsonfy();
        }
        else {
            resp.setStatusCode(GenericResponse.INVALID_PARAMETER_CODE);
            resp.setStatusReason(GenericResponse.INVALID_PARAMETER_MESSAGE);
            return resp.jsonfy();
        }
    }

    @GetMapping("/testService")
    public String testService(){
        //System.out.println(searchUtilities.createIngredientsDemo());
        System.out.println(searchUtilities.createIPastryDemo());
        System.out.println(searchUtilities.createSeasoningDemo());
        System.out.println(searchUtilities.createPizzaDemo());
        //System.out.println(searchUtilities.searchPizzaDemo());
        return null;
    }

    @GetMapping("/testMargheritaSearch")
    public String testMargheritaSearch(){
        System.out.println(searchUtilities.searchPizzaDemo());
        return null;
    }

    @GetMapping("/debugSearchMenuForPizza")
    public String debugSearchMenuForPizza(@RequestHeader(value = "Authorization", required = false) String idToken,
                                              @RequestParam(name="pizzaId") long pizzaId){
        Optional<ManagerAccount> mng= authService.validateManagerIdToken(idToken);
        if(mng.isPresent()){
            ManagerAccount manager = mng.get();
            Menu m = genService.getMenuFromPizzeriaInfoInternal((Long)manager.getPizzeria().getId());
            System.out.println(m.toString());
            menuService.debugSearchMenuForPizza(pizzaId);
        }
        return null;
    }

    @GetMapping("/debugSearchMenuForAddition")
    public String debugSearchMenuForAddition(@RequestParam(name="additionId") long additionId){
        menuService.debugSearchMenuForAddition(additionId);
        return null;
    }

    @GetMapping("/debugGetMenuRowForPizza")
    public Menu debugGetMenuRowForPizza(@RequestParam(name="pizzaId") long pizzaId, @RequestParam(name="pizzeriaId") long pizzeriaId){
        return menuService.debugSearchMenuRowForPizza(pizzaId,pizzeriaId);
    }

    @GetMapping("/debugGetMenuRowForIngredient")
    public Menu debugGetMenuRowForIngredient(@RequestParam(name="additionId") long additionId, @RequestParam(name="pizzeriaId") long pizzeriaId){
        return menuService.debugSearchMenuRowForIngredient(additionId,pizzeriaId);
    }


}

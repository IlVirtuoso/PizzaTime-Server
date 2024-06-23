package com.pizzatime.pizzaengine.Controller;


import com.pizzatime.pizzaengine.Component.GenericResponse;
import com.pizzatime.pizzaengine.Model.Menu;
import com.pizzatime.pizzaengine.Model.Seasoning;
import com.pizzatime.pizzaengine.Service.MenuService;
import com.pizzatime.pizzaengine.Service.PizzaEngineService;
import com.pizzatime.pizzaengine.Service.PizzaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/search/v1")
public class SearchEngineController {

    @Autowired
    PizzaEngineService searchUtilities;

    @Autowired
    PizzaService pizzaService;

    @Autowired
    MenuService menuService;


    @GetMapping("/getAllPizza")
    public String getAllPizza(){
        return pizzaService.getAllPizzas();
    }

    @GetMapping("/getAllPastry")
    public String getAllPastry(){
        return pizzaService.getAllPastry();
    }

    @GetMapping("/getAllSeasoning")
    public String getAllSeasoning(){
        return pizzaService.getAllSeasoning();
    }

    @GetMapping("/getAllIngredient")
    public String getAllIngredient(){
        return pizzaService.getAllIngredient();
    }

    /** INTERNAL USE */


    @GetMapping("/searchMenuForPizza")
    public List<Menu> searchMenuForPizza(@RequestParam(name="pizzaId") long pizzaId){
        return menuService.searchMenuForPizza(pizzaId);
    }

    @GetMapping("/searchMenuForAddition")
    public List<Menu> searchMenuForAddition(@RequestParam(name="additionId") long additionId){
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
    public String debugSearchMenuForPizza(@RequestParam(name="pizzaId") long pizzaId){
        menuService.debugSearchMenuForPizza(pizzaId);
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


}

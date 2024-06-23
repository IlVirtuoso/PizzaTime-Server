package com.pizzatime.pizzaengine.Controller;

import com.pizzatime.pizzaengine.Component.GenericResponse;
import com.pizzatime.pizzaengine.Service.MenuService;
import com.pizzatime.pizzaengine.Service.PizzaEngineService;
import com.pizzatime.pizzaengine.Service.PizzaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("/item/v1")
public class itemController {

    public static final boolean debug = true;

    @Autowired
    PizzaEngineService searchUtilities;

    @Autowired
    PizzaService pizzaService;

    @Autowired
    MenuService menuService;

    /** Inner class per la Request della changePassword*/
    public static class createPizzeriaRequest{
        public ArrayList<Long> ingredient;
        public String commonName;
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


}

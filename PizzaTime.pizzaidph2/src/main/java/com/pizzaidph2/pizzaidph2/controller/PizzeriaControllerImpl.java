package com.pizzaidph2.pizzaidph2.controller;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.pizzaidph2.pizzaidph2.Component.GenericResponse;
import com.pizzaidph2.pizzaidph2.model.Account;
import com.pizzaidph2.pizzaidph2.model.Pizzeria;
import com.pizzaidph2.pizzaidph2.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

//@CrossOrigin(origins="http://localhost:4200")
@RestController
@RequestMapping("/pizzeria/v1")
public class PizzeriaControllerImpl {

    @Autowired
    private PizzeriaService pizzaService;

    @Autowired
    private JWTServiceImpl jwtService;

    @Autowired
    private GeneralService genService;

    /**
     * API che crea una pizzeria associata ad un certo utente (detto manager)
     * @param sessionToken
     * @param pizzeria
     * @return
     */
    @PostMapping("/createPizzeria")
    public String createPizzeria(@RequestHeader(value="Authorization", required = false) String sessionToken,
                                 @RequestBody() Pizzeria pizzeria){
        GenericResponse resp = new GenericResponse();
        DecodedJWT jwt = jwtService.verifySession(sessionToken);
        if(jwt!=null && !jwt.equals("")) {
            GenericResponse result = pizzaService.createPizzeriaService(Long.parseLong(jwt.getSubject()), pizzeria);
            if(result.getStatusCode() == GenericResponse.OK_CODE){
                String newSessionToken = jwtService.getSessionToken(genService.getInternalAccountInfo(Long.parseLong(jwt.getSubject())));
                resp.setSessionToken(newSessionToken);
                return resp.jsonfy();
            }

        }
        resp.setStatusCode(GenericResponse.FAILED_AUTHENTICATION_CODE);
        resp.setStatusReason(GenericResponse.FAILED_AUTHENTICATION_MESSAGE);
        return resp.jsonfy();
    }




    /** LO FA PIZZAENGINE
     * API che apre/chiude una pizzeria
     * @param sessionToken
     * @return
    @GetMapping("/openPizzeria")
    public String openPizzeria(@RequestHeader(value="Authorization", required = false) String sessionToken){
        GenericResponse resp = new GenericResponse();
        DecodedJWT jwt = jwtService.verifySession(sessionToken);
        if(jwt!=null && !jwt.equals("") && !jwt.getClaim("pizzeriaID").isNull()) {
            return pizzaService.openPizzeriaService(Long.parseLong(jwt.getClaim("pizzeriaID").toString()));
        }
        resp.setStatusCode(GenericResponse.FAILED_AUTHENTICATION_CODE);
        resp.setStatusReason(GenericResponse.FAILED_AUTHENTICATION_MESSAGE);
        return resp.jsonfy();
    }
    */

    /**
     * API che resituisce tutte le informazioni su una pizzeria
     * @param sessionToken
     * @return
     */
    @GetMapping("/getPizzeriaInfo")
    public String getPizzeria(@RequestHeader(value="Authorization", required = false) String sessionToken){
        GenericResponse resp = new GenericResponse();
        DecodedJWT jwt = jwtService.verifySession(sessionToken);
        if(jwt!=null && !jwt.equals("") && !jwt.getClaim("pizzeriaID").isNull()) {
            return pizzaService.getPizzeriaInfo(Long.parseLong(jwt.getClaim("pizzeriaID").toString()));
        }
        resp.setStatusCode(GenericResponse.FAILED_AUTHENTICATION_CODE);
        resp.setStatusReason(GenericResponse.FAILED_AUTHENTICATION_MESSAGE);
        return resp.jsonfy();
    }



}

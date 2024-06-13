package com.PizzaTime.OrderService

import com.PizzaTime.OrderService.Services.CommunicationService
import com.PizzaTime.OrderService.Services.OrderService
import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.HttpHeaders
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.context.WebApplicationContext

@Controller
class OrderController(
    var orderService: OrderService,
    var communicationService: CommunicationService
){


    @GetMapping("/api/v1/order/{id}")
    fun getById(id: String): Order{
        return orderService.getOrderById(id)!!;
    }

    class OrderSubmitRequest{
        var pizzeriaId: String = "";
        var pizzas : List<Long>? = null;
    }

    @PostMapping("/api/v1/order/submit")
    fun submitOrder(@RequestHeader(HttpHeaders.AUTHORIZATION) userToken: String, @RequestBody submitRequest: OrderSubmitRequest): Unit? {
        var userId = communicationService.getUserFromToken(userToken);
        var order = Order();
        order.pizzeriaId = submitRequest.pizzeriaId;
        order.pizzas = submitRequest.pizzas;
        order.userId = userId;
        order.totalPrice = communicationService.getAllPizzasPrice(submitRequest.pizzas!!);
        return orderService.save(order);
    }



}
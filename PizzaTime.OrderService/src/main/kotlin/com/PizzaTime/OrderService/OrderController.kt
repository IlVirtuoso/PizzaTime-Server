package com.PizzaTime.OrderService

import com.PizzaTime.OrderService.Services.ICommunicationService
import com.PizzaTime.OrderService.Services.IOrderService
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*

@Controller
class OrderController(
    var orderService: IOrderService,
    var communicationService: ICommunicationService,
) {


    @GetMapping("/api/v1/order/{id}")
    fun getById(@PathVariable id: String): Order {
        return orderService.getOrderById(id);
    }


    @PostMapping("/api/v1/order/create")
    fun create_order(@RequestHeader(HttpHeaders.AUTHORIZATION) sessionToken: String) : String {
        var order = Order();
        order = orderService.save(order);
        communicationService.notifyOrderCreate(sessionToken,order);
        return order.id;
    }

    @GetMapping("/api/v1/order/pizzeria/{pizzeriaid}/orders/accepted")
    fun get_accepted_orders(@PathVariable pizzeriaid: String) : Collection<Order>{
        val orders = orderService.getOrdersForPizzeria(pizzeriaid, OrderStatus.ACCEPTED);
        return orders;
    }

    @GetMapping("/api/v1/order/pizzeria/{pizzeriaid}/orders/pending")
    fun get_pending_orders(@PathVariable pizzeriaid : String) : Collection<Order>{
        val orders = orderService.getOrdersForPizzeria(pizzeriaid,OrderStatus.QUEUED);
        return orders;
    }



    @PostMapping("/api/v1/order/{id}/addPizza")
    fun add_pizza(@RequestHeader(HttpHeaders.AUTHORIZATION) userToken : String ,@PathVariable id: String, @RequestBody pizzaId: Long): Boolean{
        val order = orderService.getOrderById(id);
        if(!order.pizzas.contains(pizzaId)){
            val set = HashSet(order.pizzas);
            set.add(pizzaId);
            order.pizzas = set;
            orderService.save(order);
            return true;
        }
        return false;
    }

    @PostMapping("/api/v1/order/{id}/removePizza")
    fun remove_pizza(@RequestHeader(HttpHeaders.AUTHORIZATION) userToken : String ,@PathVariable id: String, @RequestBody pizzaId: Long): Boolean{
        val order = orderService.getOrderById(id)
        if(order.pizzas.contains(pizzaId)){
            val set = HashSet(order.pizzas);
            set.remove(pizzaId);
            order.pizzas = set;
            orderService.save(order);
            return true;
        }
        return false
    }







}
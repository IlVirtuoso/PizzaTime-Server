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
    fun getById(id: String): Order {
        return orderService.getOrderById(id);
    }


    @PostMapping("/api/v1/order/create")
    fun create_order(@RequestHeader(HttpHeaders.AUTHORIZATION) sessionToken: String, @RequestBody pizzas: List<Long>) : String {
        var order = Order();
        order.pizzas = pizzas;
        order = orderService.save(order);
        communicationService.notifyOrderCreate(sessionToken,order);
        return order.id;
    }

    fun confirm_authorization(orderId: String){
        var order = orderService.getOrderById(orderId)
        order.orderStatus = OrderStatus.ACCEPTED;
        communicationService.notifyOrderAccepted(order)
    }

    fun confirm_accepted(orderId: String, piva: String ){
        val order = orderService.getOrderById(orderId)
        order.orderStatus = OrderStatus.SERVING;
        communicationService.notifyOrderServing(order)
    }


}
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
    private val httpServletResponse: HttpServletResponse
) {


    @GetMapping("/api/v1/order/{id}")
    fun getById(id: String): Order {
        return orderService.getOrderById(id);
    }

    class OrderSubmitRequest {
        var pizzeriaId: String = "";
        var pizzas: List<Long>? = null;
    }

    @PostMapping("/api/v1/order/submit")
    suspend fun submitOrder(
        @RequestHeader(HttpHeaders.AUTHORIZATION) userToken: String,
        @RequestBody submitRequest: OrderSubmitRequest
    ): Unit? {
        var userId = communicationService.getUserFromToken(userToken);
        var order = Order();
        order.pizzeriaId = submitRequest.pizzeriaId;
        order.totalPrice = communicationService.sumPizzasPrice(submitRequest.pizzas!!).await();
        order.pizzas = submitRequest.pizzas;
        order.userId = userId.await();
        return orderService.save(order);
    }

    private suspend fun SetOrderState(userToken: String, id: String, state: OrderStatus): Boolean {
        val userId = communicationService.getUserFromToken(userToken);
        val order = orderService.getOrderById(id);
        if (order.orderStatus < state) {
            order.orderStatus = state;
            orderService.save(order);
            return true;
        }
        httpServletResponse.status = HttpServletResponse.SC_UNAUTHORIZED;
        return false;
    }


    @PostMapping("/api/v1/order/{id}/cancel")
    suspend fun cancelOrder(
        @RequestHeader(HttpHeaders.AUTHORIZATION) userToken: String,
        @PathVariable id: String
    ): Boolean {
        return SetOrderState(userToken, id, OrderStatus.CANCELED);
    }

    @PostMapping("/api/v1/order/{id}/accept")
    suspend fun acceptOrder(
        @RequestHeader(HttpHeaders.AUTHORIZATION) userToken: String,
        @PathVariable id: String
    ): Boolean {
        val order = orderService.getOrderById(id);
        if (!communicationService.isUserAdminForPizzeria(userToken, order.pizzeriaId).await()) {
            httpServletResponse.status = HttpServletResponse.SC_UNAUTHORIZED;
            return false;
        }
        return SetOrderState(userToken, id, OrderStatus.SERVING);
    }

    @PostMapping("/api/v1/order/{id}/refuse")
    suspend fun refuseOrder(
        @RequestHeader(HttpHeaders.AUTHORIZATION) userToken: String,
        @PathVariable id: String
    ): Boolean {
        val order = orderService.getOrderById(id);
        if (!communicationService.isUserAdminForPizzeria(userToken, order.pizzeriaId).await()) {
            httpServletResponse.status = HttpServletResponse.SC_UNAUTHORIZED;
            return false;
        }
        return SetOrderState(userToken, id, OrderStatus.REFUSED);
    }

    @PostMapping("/api/v1/order/{id}/complete")
    suspend fun completeOrder(
        @RequestHeader(HttpHeaders.AUTHORIZATION) userToken: String,
        @PathVariable id: String
    ): Boolean {
        val order = orderService.getOrderById(id);
        if (!communicationService.isUserAdminForPizzeria(userToken, order.pizzeriaId).await()) {
            httpServletResponse.status = HttpServletResponse.SC_UNAUTHORIZED;
            return false;
        }
        return SetOrderState(userToken, id, OrderStatus.COMPLETED);
    }

    @GetMapping("/api/v1/order/getOrdersForPizzeria")
    suspend fun getOrdersForPizzeria(
        @RequestHeader(HttpHeaders.AUTHORIZATION) userToken: String,
        pizzeriaId: String
    ): List<Order> {
        if (communicationService.isUserAdminForPizzeria(userToken, pizzeriaId).await()) {
            return orderService.getPizzeriaOrders(pizzeriaId).toList();
        }
        httpServletResponse.status = HttpServletResponse.SC_UNAUTHORIZED;
        return emptyList();
    }


}
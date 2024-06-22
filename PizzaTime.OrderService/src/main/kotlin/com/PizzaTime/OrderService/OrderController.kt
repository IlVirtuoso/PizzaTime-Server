package com.PizzaTime.OrderService

import com.PizzaTime.OrderService.Messages.ErrorResponse
import com.PizzaTime.OrderService.Messages.GenericOrderResponse
import com.PizzaTime.OrderService.Messages.ResponseMessage
import com.PizzaTime.OrderService.Model.Order
import com.PizzaTime.OrderService.Model.OrderRow
import com.PizzaTime.OrderService.Model.OrderStatus
import com.PizzaTime.OrderService.Services.ICommunicationService
import com.PizzaTime.OrderService.Services.IOrderService
import com.PizzaTime.OrderService.Services.IUserAuthorizationService
import jakarta.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*

@Controller
class OrderController(
    var orderService: IOrderService,
    var communicationService: ICommunicationService,
    var userAuthorizationService: IUserAuthorizationService
) {


    @GetMapping("/api/v1/order/{id}")
    fun getById(@PathVariable id: String): Order {
        return orderService.getOrderById(id);
    }


    @PostMapping("/api/v1/order/create")
    fun create_order(@RequestHeader(HttpHeaders.AUTHORIZATION) sessionToken: String, @Autowired response: HttpServletResponse) : GenericOrderResponse{
        val token = userAuthorizationService.validateUserIdToken(sessionToken);
        if(token.statusCode == HttpServletResponse.SC_UNAUTHORIZED){
            response.status = HttpStatus.UNAUTHORIZED.value();
            return ErrorResponse("Unauthorized");
        }
        var order = Order();
        order.userId = token.account!!.id;
        order = orderService.save(order);
        communicationService.notifyOrderCreate(sessionToken,order);
        return ResponseMessage(order);
    }


    @GetMapping("/api/v1/order/pizzeria/{pizzeriaid}/orders/accepted")
    fun get_accepted_orders(@RequestHeader(HttpHeaders.AUTHORIZATION) userToken : String,@PathVariable pizzeriaid: String, @Autowired httpServletResponse: HttpServletResponse) : Collection<Order>?{
        val token = userAuthorizationService.validateUserIdToken(pizzeriaid);
        if(token.statusCode == HttpServletResponse.SC_UNAUTHORIZED){
            httpServletResponse.status = HttpStatus.UNAUTHORIZED.value();
            return null;
        }
        val orders = orderService.getOrdersForPizzeria(pizzeriaid, OrderStatus.ACCEPTED);
        return orders;
    }

    @GetMapping("/api/v1/order/pizzeria/{pizzeriaid}/orders/pending")
    fun get_pending_orders(@PathVariable pizzeriaid : String, @Autowired httpServletResponse: HttpServletResponse) : Collection<Order>? {
        val token = userAuthorizationService.validateUserIdToken(pizzeriaid);
        if(token.statusCode == HttpServletResponse.SC_UNAUTHORIZED){
            httpServletResponse.status = HttpStatus.UNAUTHORIZED.value();
            return null;
        }
        val orders = orderService.getOrdersForPizzeria(pizzeriaid, OrderStatus.QUEUED);
        return orders;
    }



   @PostMapping("/api/v1/order/{pizzeriaId}/{orderId}/accept")
   fun accept_order(@RequestHeader(HttpHeaders.AUTHORIZATION) userToken: String ,@PathVariable pizzeriaId : String, @PathVariable orderId : String, @Autowired httpServletResponse: HttpServletResponse) : GenericOrderResponse{
       val token = userAuthorizationService.validateUserIdToken(pizzeriaId);
       if(token.statusCode == HttpServletResponse.SC_UNAUTHORIZED){
           httpServletResponse.status = HttpStatus.UNAUTHORIZED.value();
           return ErrorResponse("Unauthorized");
       }
       val order = orderService.getOrderById(orderId);
       if(order.orderStatus != OrderStatus.READY.status){
           httpServletResponse.status = HttpStatus.FORBIDDEN.value();
           return ErrorResponse("OrderAlreadyAccepted");
       }
       order.pizzeriaId = pizzeriaId;
       order.orderStatus = OrderStatus.ACCEPTED.status;
       return ResponseMessage("OK");
   }

    data class OrderRowRequest(val baseId : Long, val pizzaId : Long, val ingredients : List<Long>)

    @PostMapping("/api/v1/order/{orderId}/addrow")
    fun add_row(@RequestHeader token: String ,@PathVariable orderId : String, @Autowired response: HttpServletResponse, @RequestBody orderRow: OrderRowRequest) : GenericOrderResponse {
        val token = userAuthorizationService.validateUserIdToken(token);
        if(token.statusCode == HttpServletResponse.SC_UNAUTHORIZED){
            response.status = HttpStatus.UNAUTHORIZED.value();
            return ErrorResponse("Unauthorized");
        }
        val order = orderService.getOrderById(orderId);
        val row = OrderRow()
        row.order = order;
        row.baseId = orderRow.baseId
        row.pizzaId = orderRow.pizzaId
        row.ingredients = HashSet(orderRow.ingredients);
        return ResponseMessage("OK");
    }






}
package com.PizzaTime.OrderService

import com.PizzaTime.OrderService.Messages.ErrorResponse
import com.PizzaTime.OrderService.Messages.GenericOrderResponse
import com.PizzaTime.OrderService.Messages.ResponseMessage
import com.PizzaTime.OrderService.Model.Order
import com.PizzaTime.OrderService.Model.OrderRow
import com.PizzaTime.OrderService.Model.OrderStatus
import com.PizzaTime.OrderService.Services.*
import jakarta.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import java.util.*
import kotlin.collections.HashSet




@Controller
class OrderController(
    var orderService: IOrderService,
    var communicationService: ICommunicationService,
    var userAuthorizationService: IUserAuthorizationService,
) {

    private class InvalidToken: Throwable();
    private class OrderNotExist : Throwable();
    private class OrderRowNotExist: Throwable();


    private fun <T> checkSequence( httpServletResponse: HttpServletResponse, fn : ()-> T) : GenericOrderResponse{
        try{
            val result = fn();
            return ResponseMessage(result)
        }
        catch (_: OrderNotExist){
            httpServletResponse.status = HttpStatus.NOT_FOUND.value()
            return ErrorResponse("Order doesn't exist")
        }
        catch (_ : OrderRowNotExist){
            httpServletResponse.status = HttpStatus.NOT_FOUND.value()
            return ErrorResponse("Order row doesn't exist")
        }
        catch (_: InvalidToken){
            httpServletResponse.status = HttpStatus.UNAUTHORIZED.value()
            return ErrorResponse("InvalidToken")
        }
        catch (ex: IllegalArgumentException){
            httpServletResponse.status = HttpStatus.BAD_REQUEST.value()
            return ErrorResponse(ex.localizedMessage)
        }
        catch (ex: Exception){
            httpServletResponse.status = HttpStatus.INTERNAL_SERVER_ERROR.value()
            return ErrorResponse(ex.message!!)
        }

    }

    fun userAuthStep(sessionToken: String) : UserToken<UserAccount>{
        val token = userAuthorizationService.validateUserIdToken(sessionToken);
        if(token.statusCode == HttpServletResponse.SC_UNAUTHORIZED){
            throw InvalidToken();
        }
        return token;
    }

    fun orderRetrieveStep(orderId: String) : Order{
        val order = orderService.getOrderById(orderId);
        if(order.isEmpty){
            throw OrderNotExist();
        }
        return order.get();
    }

    fun adminAuthStep(sessionToken: String) : UserToken<ManagerAccount>{
        val token = userAuthorizationService.validateManagerIdToken(sessionToken);
        if(token.statusCode == HttpServletResponse.SC_UNAUTHORIZED){
            throw InvalidToken();
        }
        return token;
    }

    @GetMapping("/api/v1/order/{id}")
    fun getById(@RequestHeader(HttpHeaders.AUTHORIZATION) sessionToken: String ,@PathVariable id: String, httpServletResponse: HttpServletResponse): GenericOrderResponse = checkSequence(httpServletResponse) {
        val token = userAuthStep(sessionToken);
        val order = orderRetrieveStep(id);
        return@checkSequence order;
    }


    @PostMapping("/api/v1/order/create")
    fun create_order(@RequestHeader(HttpHeaders.AUTHORIZATION) sessionToken: String, @Autowired response: HttpServletResponse) : GenericOrderResponse = checkSequence(response) {
        val token = userAuthStep(sessionToken);
        var order = Order();
        order.userId = token.account!!.id;
        order = orderService.save(order);
        communicationService.notifyOrderCreate(sessionToken,order);
        return@checkSequence order;
    }


    @GetMapping("/api/v1/order/pizzeria/{pizzeriaid}/orders/accepted")
    fun get_accepted_orders(@RequestHeader(HttpHeaders.AUTHORIZATION) adminToken : String,@PathVariable pizzeriaid: String, @Autowired httpServletResponse: HttpServletResponse) : GenericOrderResponse = checkSequence(httpServletResponse) {
        adminAuthStep(adminToken);
        val orders = orderService.getOrdersForPizzeria(pizzeriaid, OrderStatus.ACCEPTED);
        return@checkSequence orders;
    }

    @GetMapping("/api/v1/order/pizzeria/{pizzeriaid}/orders/pending")
    fun get_pending_orders(@RequestHeader(HttpHeaders.AUTHORIZATION) adminToken: String ,@PathVariable pizzeriaid : String, @Autowired httpServletResponse: HttpServletResponse) : Collection<Order>? {
        val token = userAuthorizationService.validateUserIdToken(pizzeriaid);
        if(token.statusCode == HttpServletResponse.SC_UNAUTHORIZED){
            httpServletResponse.status = HttpStatus.UNAUTHORIZED.value();
            return null;
        }
        val orders = orderService.getOrdersForPizzeria(pizzeriaid, OrderStatus.QUEUED);
        return orders;
    }



   @PostMapping("/api/v1/order/{pizzeriaId}/{orderId}/accept")
   fun accept_order(@RequestHeader(HttpHeaders.AUTHORIZATION) adminToken: String ,@PathVariable pizzeriaId : String, @PathVariable orderId : String, @Autowired httpServletResponse: HttpServletResponse) : GenericOrderResponse = checkSequence(httpServletResponse){
       val token = adminAuthStep(adminToken);
       var order = orderRetrieveStep(orderId);
       if(order.orderStatus != OrderStatus.READY.status){
           throw IllegalArgumentException("Order already accepted");
       }
       order.pizzeriaId = pizzeriaId;
       order.orderStatus = OrderStatus.ACCEPTED.status;
       order = orderService.save(order);
       return@checkSequence order;
   }

    data class OrderRowRequest(val baseId : Long, val pizzaId : Long, val ingredients : List<Long>, val quantity: Int = 1)

    @PostMapping("/api/v1/order/{orderId}/addrow")
    fun add_row(@RequestHeader token: String ,@PathVariable orderId : String, @Autowired response: HttpServletResponse, @RequestBody orderRow: OrderRowRequest) : GenericOrderResponse = checkSequence(response){
        val token = userAuthStep(token);
        val order = orderRetrieveStep(orderId);
        val row = OrderRow()
        row.order = order;
        row.baseId = orderRow.baseId
        row.pizzaId = orderRow.pizzaId
        row.ingredients = HashSet(orderRow.ingredients);
        row.quantity = orderRow.quantity;
        order.orderRows = HashSet(order.orderRows) + row;
        orderService.save(order);
        orderService.saveRow(row);
        return@checkSequence row;
    }




    @DeleteMapping("/api/v1/order/{orderId}/removeRow")
    fun remove_row(@RequestHeader token: String, @PathVariable orderId : String, httpServletResponse: HttpServletResponse, @RequestBody lineId: Long ) : GenericOrderResponse = checkSequence(httpServletResponse){
        val token = userAuthStep(token);
        orderService.deleteOrderRow(orderId,lineId);
        return@checkSequence "OK";
    }






}
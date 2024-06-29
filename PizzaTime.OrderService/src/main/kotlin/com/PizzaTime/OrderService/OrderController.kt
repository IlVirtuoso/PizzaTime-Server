package com.PizzaTime.OrderService

import com.PizzaTime.OrderService.Messages.*
import com.PizzaTime.OrderService.Model.Order
import com.PizzaTime.OrderService.Model.OrderRow
import com.PizzaTime.OrderService.Model.OrderStatus
import com.PizzaTime.OrderService.Services.*
import com.PizzaTime.OrderService.Services.Amqp.ICommunicationService
import com.PizzaTime.OrderService.Services.Amqp.IUserAuthorizationService
import com.PizzaTime.OrderService.Services.Amqp.ManagerAccount
import com.PizzaTime.OrderService.Services.Amqp.UserAccount
import jakarta.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import kotlin.collections.HashSet


@RestController
class OrderController(
    var orderService: IOrderService,
    var communicationService: ICommunicationService,
    var userAuthorizationService: IUserAuthorizationService,
    var response: HttpServletResponse
) {

    private class InvalidToken : Throwable();
    private class OrderNotExist : Throwable();
    private class OrderRowNotExist : Throwable();


    private fun <T> checkSequence(fn: () -> T): GenericOrderResponse {
        try {
            val result = fn();
            return ResultResponse(result);
        } catch (_: OrderNotExist) {
            response.status = HttpStatus.NOT_FOUND.value()
            return ErrorResponse("Order doesn't exist")
        } catch (_: OrderRowNotExist) {
            response.status = HttpStatus.NOT_FOUND.value()
            return ErrorResponse("Order row doesn't exist")
        } catch (_: InvalidToken) {
            response.status = HttpStatus.UNAUTHORIZED.value()
            return ErrorResponse("InvalidToken")
        } catch (ex: IllegalArgumentException) {
            response.status = HttpStatus.BAD_REQUEST.value()
            return ErrorResponse(ex.localizedMessage)
        } catch (ex: Exception) {
            response.status = HttpStatus.INTERNAL_SERVER_ERROR.value()
            return ErrorResponse(ex.message!!)
        }

    }

    fun userAuthStep(sessionToken: String): UserAccount {
        val token = userAuthorizationService.validateUserIdToken(sessionToken);
        if (token.isEmpty()) {
            throw InvalidToken();
        }
        return token.get();
    }

    fun orderRetrieveStep(orderId: String): Order {
        val order = orderService.getOrderById(orderId);
        if (order.isEmpty) {
            throw OrderNotExist();
        }
        return order.get();
    }

    fun adminAuthStep(sessionToken: String): ManagerAccount {
        val token = userAuthorizationService.validateManagerIdToken(sessionToken);
        if (token.isEmpty()) {
            throw InvalidToken();
        }
        return token.get();
    }

    /***
     * @return an order by id
     */
    @GetMapping("/api/v1/order/{id}")
    fun getById(
        @RequestHeader(HttpHeaders.AUTHORIZATION) sessionToken: String,
        @PathVariable id: String,
    ): GenericOrderResponse = checkSequence{
        val token = userAuthStep(sessionToken);
        val order = orderRetrieveStep(id);
        return@checkSequence order;
    }


    @PostMapping("/api/v1/order/create")
    fun create_order(
        @RequestHeader(HttpHeaders.AUTHORIZATION) sessionToken: String
    ): GenericOrderResponse = checkSequence {
        val token = userAuthStep(sessionToken);
        var order = Order();
        order.userId = token.id;
        order.address = token.address;
        order = orderService.save(order);
        communicationService.notifyOrderCreate(order);
        return@checkSequence order;
    }


    @GetMapping("/api/v1/order/{pizzeriaId}/orders")
    fun get_pizzeria_orders(
        @RequestHeader(HttpHeaders.AUTHORIZATION) adminToken: String,
        @PathVariable pizzeriaId: String,

    ):
            GenericOrderResponse = checkSequence{
        val token = adminAuthStep(pizzeriaId);
        val orders = orderService.getOrdersForPizzeria(pizzeriaId);
        return@checkSequence orders;
    }

    @GetMapping("/api/v1/order/{pizzeriaId}/history")
    fun get_history(
        @RequestHeader(HttpHeaders.AUTHORIZATION) adminToken: String,
        @PathVariable pizzeriaId: String,
    ) : GenericOrderResponse = checkSequence {
        val token = adminAuthStep(pizzeriaId);
        val orders = orderService.getOrdersForPizzeria(pizzeriaId);
    }


    @PostMapping("/api/v1/order/{orderId}/submit")
    fun submit_order(
        @RequestHeader(HttpHeaders.AUTHORIZATION) userToken: String,
        @PathVariable orderId: String,

    ) = checkSequence {
        val token = userAuthStep(orderId);
        val order = orderRetrieveStep(orderId);
        order.orderStatus = OrderStatus.QUEUED.status;
        orderService.save(order);
        communicationService.notifyOrderStatusChanged(order);
        return@checkSequence order;
    }


    data class OrderRowRequest(val baseId: Long, val pizzaId: Long, val ingredients: List<Long>, val quantity: Int = 1)

    @PostMapping("/api/v1/order/{orderId}/addrow")
    fun add_row(
        @RequestHeader token: String,
        @PathVariable orderId: String,
        @RequestBody orderRow: OrderRowRequest,
    ): GenericOrderResponse = checkSequence {
        val token = userAuthStep(token);
        var order = orderRetrieveStep(orderId);
        if(order.orderStatus != OrderStatus.READY.status) {
            throw IllegalArgumentException("Cannot edit a non ready order");
        }
        var row = OrderRow()
        row.order = order;
        row.baseId = orderRow.baseId
        row.pizzaId = orderRow.pizzaId
        row.ingredients = HashSet(orderRow.ingredients);
        row.quantity = orderRow.quantity;
        row = orderService.saveRow(order, row);
        return@checkSequence row;
    }


    @DeleteMapping("/api/v1/order/{orderId}/{lineId}/removeRow")
    fun remove_row(
        @RequestHeader token: String,
        @PathVariable orderId: String,
        @PathVariable lineId: Long,
    ): GenericOrderResponse = checkSequence{
        userAuthStep(token);
        val order = orderRetrieveStep(orderId);
        if(order.orderStatus != OrderStatus.READY.status) {
            throw IllegalArgumentException("Cannot edit a non ready order");
        }
        val row = order.orderRows.find { t -> t.id == lineId }
        if (row == null) {
            throw IllegalArgumentException("Row not found")
        }
        orderService.deleteOrderRow(order, row);
        return@checkSequence "OK";
    }


    @PostMapping("/api/v1/order/{orderId}/cancel")
    fun cancel_order(
        @RequestHeader token: String,
        @PathVariable orderId: String,
    ): GenericOrderResponse = checkSequence{
        userAuthStep(token);
        val order = orderRetrieveStep(orderId);
        if(order.orderStatus != OrderStatus.QUEUED.status) {
            throw IllegalArgumentException("Cannot cancel a non queued order")
        }
        order.orderStatus = OrderStatus.CANCELED.status;
        communicationService.notifyOrderCancellation(order);
        return@checkSequence orderService.save(order)
    }


    @PostMapping("/api/v1/order/{orderId}/refuse")
    fun refuse_order(
        @RequestHeader token: String,
        @PathVariable orderId: String,
    ): GenericOrderResponse = checkSequence {
        adminAuthStep(token);
        val order = orderRetrieveStep(orderId);
        if (order.orderStatus != OrderStatus.QUEUED.status) {
            throw IllegalArgumentException("Order not in queued status");
        }
        order.orderStatus = OrderStatus.REFUSED.status;
        communicationService.notifyOrderStatusChanged(order);
        return@checkSequence "OK";
    }

    @PostMapping("/api/v1/order/{pizzeriaId}/{orderId}/accept")
    fun accept_order(
        @RequestHeader(HttpHeaders.AUTHORIZATION) adminToken: String,
        @PathVariable pizzeriaId: Long,
        @PathVariable orderId: String,
    ): GenericOrderResponse = checkSequence{
        val token = adminAuthStep(adminToken);
        var order = orderRetrieveStep(orderId);
        if (order.orderStatus != OrderStatus.QUEUED.status) {
            throw IllegalArgumentException("Cannot accept a non queued order");
        }
        order.pizzeriaId = pizzeriaId;
        order.orderStatus = OrderStatus.ACCEPTED.status;
        order = orderService.save(order);
        communicationService.notifyOrderStatusChanged(order)
        return@checkSequence order;
    }



}
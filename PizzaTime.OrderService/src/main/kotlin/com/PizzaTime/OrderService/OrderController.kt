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

    private class InvalidToken : Throwable();
    private class OrderNotExist : Throwable();
    private class OrderRowNotExist : Throwable();


    private fun <T> checkSequence(httpServletResponse: HttpServletResponse, fn: () -> T): GenericOrderResponse {
        try {
            val result = fn();
            return ResponseMessage(result)
        } catch (_: OrderNotExist) {
            httpServletResponse.status = HttpStatus.NOT_FOUND.value()
            return ErrorResponse("Order doesn't exist")
        } catch (_: OrderRowNotExist) {
            httpServletResponse.status = HttpStatus.NOT_FOUND.value()
            return ErrorResponse("Order row doesn't exist")
        } catch (_: InvalidToken) {
            httpServletResponse.status = HttpStatus.UNAUTHORIZED.value()
            return ErrorResponse("InvalidToken")
        } catch (ex: IllegalArgumentException) {
            httpServletResponse.status = HttpStatus.BAD_REQUEST.value()
            return ErrorResponse(ex.localizedMessage)
        } catch (ex: Exception) {
            httpServletResponse.status = HttpStatus.INTERNAL_SERVER_ERROR.value()
            return ErrorResponse(ex.message!!)
        }

    }

    fun userAuthStep(sessionToken: String): UserToken<UserAccount> {
        val token = userAuthorizationService.validateUserIdToken(sessionToken);
        if (token.statusCode == HttpServletResponse.SC_UNAUTHORIZED) {
            throw InvalidToken();
        }
        return token;
    }

    fun orderRetrieveStep(orderId: String): Order {
        val order = orderService.getOrderById(orderId);
        if (order.isEmpty) {
            throw OrderNotExist();
        }
        return order.get();
    }

    fun adminAuthStep(sessionToken: String): UserToken<ManagerAccount> {
        val token = userAuthorizationService.validateManagerIdToken(sessionToken);
        if (token.statusCode == HttpServletResponse.SC_UNAUTHORIZED) {
            throw InvalidToken();
        }
        return token;
    }

    @GetMapping("/api/v1/order/{id}")
    fun getById(
        @RequestHeader(HttpHeaders.AUTHORIZATION) sessionToken: String,
        @PathVariable id: String,
        httpServletResponse: HttpServletResponse,
    ): GenericOrderResponse = checkSequence(httpServletResponse) {
        val token = userAuthStep(sessionToken);
        val order = orderRetrieveStep(id);
        return@checkSequence order;
    }


    @PostMapping("/api/v1/order/create")
    fun create_order(
        @RequestHeader(HttpHeaders.AUTHORIZATION) sessionToken: String,
        @Autowired response: HttpServletResponse,
    ): GenericOrderResponse = checkSequence(response) {
        val token = userAuthStep(sessionToken);
        var order = Order();
        order.userId = token.account!!.id;
        order = orderService.save(order);
        communicationService.notifyOrderCreate(order);
        return@checkSequence order;
    }


    @GetMapping("/api/v1/order/{pizzeriaId}/orders")
    fun get_pizzeria_orders(
        @RequestHeader(HttpHeaders.AUTHORIZATION) adminToken: String,
        @PathVariable pizzeriaId: String,
        httpServletResponse: HttpServletResponse,
    ):
            GenericOrderResponse = checkSequence(httpServletResponse)
    {
        val token = adminAuthStep(pizzeriaId);
        val orders = orderService.getOrdersForPizzeria(pizzeriaId);
        return@checkSequence orders;
    }

    @GetMapping("/api/v1/order/{pizzeriaId}/history")
    fun get_history(
        @RequestHeader(HttpHeaders.AUTHORIZATION) adminToken: String,
        @PathVariable pizzeriaId: String,
        httpServletResponse: HttpServletResponse,
    ) : GenericOrderResponse = checkSequence(httpServletResponse) {
        val token = adminAuthStep(pizzeriaId);
        val orders = orderService.getOrdersForPizzeria(pizzeriaId);
    }


    @PostMapping("/api/v1/order/{pizzeriaId}/{orderId}/accept")
    fun accept_order(
        @RequestHeader(HttpHeaders.AUTHORIZATION) adminToken: String,
        @PathVariable pizzeriaId: String,
        @PathVariable orderId: String,
        @Autowired httpServletResponse: HttpServletResponse,
    ): GenericOrderResponse = checkSequence(httpServletResponse) {
        val token = adminAuthStep(adminToken);
        var order = orderRetrieveStep(orderId);
        if (order.orderStatus != OrderStatus.READY.status) {
            throw IllegalArgumentException("Order already accepted");
        }
        order.pizzeriaId = pizzeriaId;
        order.orderStatus = OrderStatus.ACCEPTED.status;
        order = orderService.save(order);
        communicationService.notifyOrderStatusChanged(order)
        return@checkSequence order;
    }

    data class OrderRowRequest(val baseId: Long, val pizzaId: Long, val ingredients: List<Long>, val quantity: Int = 1)

    @PostMapping("/api/v1/order/{orderId}/addrow")
    fun add_row(
        @RequestHeader token: String,
        @PathVariable orderId: String,
        @Autowired response: HttpServletResponse,
        @RequestBody orderRow: OrderRowRequest,
    ): GenericOrderResponse = checkSequence(response) {
        val token = userAuthStep(token);
        var order = orderRetrieveStep(orderId);
        var row = OrderRow()
        row.order = order;
        row.baseId = orderRow.baseId
        row.pizzaId = orderRow.pizzaId
        row.ingredients = HashSet(orderRow.ingredients);
        row.quantity = orderRow.quantity;
        row = orderService.saveRow(order, row);
        return@checkSequence row;
    }


    @DeleteMapping("/api/v1/order/{orderId}/removeRow")
    fun remove_row(
        @RequestHeader token: String,
        @PathVariable orderId: String,
        httpServletResponse: HttpServletResponse,
        @RequestBody lineId: Long,
    ): GenericOrderResponse = checkSequence(httpServletResponse) {
        userAuthStep(token);
        val order = orderRetrieveStep(orderId);
        val row = order.orderRows.find { t -> t.id == lineId }
        if (row == null) {
            throw IllegalArgumentException("Row not found")
        }
        orderService.deleteOrderRow(order, row);
        return@checkSequence "OK";
    }


    @PostMapping("api/v1/order/{orderId}/cancel")
    fun cancel_order(
        @RequestHeader token: String,
        @PathVariable orderId: String,
        httpServletResponse: HttpServletResponse,
    ): GenericOrderResponse = checkSequence(httpServletResponse) {
        userAuthStep(token);
        val order = orderRetrieveStep(orderId);
        when (order.orderStatus) {
            OrderStatus.CANCELED.status -> {
                throw IllegalArgumentException("Cannot cancel already canceled order")
            }

            OrderStatus.COMPLETED.status -> {
                throw IllegalArgumentException("Cannot cancel already completed order")
            }

            OrderStatus.SERVING.status -> {
                throw IllegalArgumentException("Cannot cancel serving order")
            }

            else -> {}
        }
        order.orderStatus = OrderStatus.CANCELED.status;
        communicationService.notifyOrderCancellation(order);
        return@checkSequence orderService.save(order)
    }


    @PostMapping("/api/v1/order/{orderId}/refuse")
    fun refuse_order(
        @RequestHeader token: String,
        @PathVariable orderId: String,
        httpServletResponse: HttpServletResponse,
    ): GenericOrderResponse = checkSequence(httpServletResponse) {
        adminAuthStep(token);
        val order = orderRetrieveStep(orderId);
        if (order.orderStatus != OrderStatus.QUEUED.status) {
            throw IllegalArgumentException("Order not in queued status");
        }
        order.orderStatus = OrderStatus.REFUSED.status;
        communicationService.notifyOrderStatusChanged(order);
        return@checkSequence "OK";
    }


}
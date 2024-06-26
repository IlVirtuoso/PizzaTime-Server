package com.PizzaTime.OrderService

import com.PizzaTime.OrderService.Messages.ErrorResponse
import com.PizzaTime.OrderService.Messages.ResultResponse
import com.PizzaTime.OrderService.Model.Order
import com.PizzaTime.OrderService.Model.OrderStatus
import com.PizzaTime.OrderService.Services.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus
import org.springframework.mock.web.MockHttpServletResponse

@SpringBootTest(
    properties = [
        "order.sagaservice.enabled=false",
        "order.userauthservice.enabled=false"
    ]
)


class OrderControllerTest {


    @Autowired
    private lateinit var userAuthorizationService: MockUserService;


    @Autowired
    private lateinit var mockResponder: MockResponder;


    @Autowired
    private lateinit var orderController: OrderController


    @Test
    fun contextLoads() {

    }


    fun create_order(): Order {
        val response = MockHttpServletResponse();
        userAuthorizationService.onValidateUserToken = { userToken: String ->

             UserAccount(10, "via mazzini")
        }
        return orderController.create_order("ciao", response).let { t -> (t as ResultResponse<Order>).load };
    }


    @Test
    fun order_creation() {
        val order = create_order();
        assert(order.orderStatus == OrderStatus.READY.status);
        assert(order.userId == 10.toLong())
        assert(order.orderRows.isEmpty());
    }

    @Test
    fun order_creation_failure() {
        val response = MockHttpServletResponse();
        userAuthorizationService.onValidateUserToken = { userToken: String ->
            UserToken<UserAccount>(HttpStatus.UNAUTHORIZED.value(), null)
        }
        val error = orderController.create_order("ciao", response).let { t -> t as ErrorResponse };
        assert(error.reason.isNotEmpty())
    }


    @Test
    fun order_add_row() {
        val response = MockHttpServletResponse();
        val sessionToken = "ciao";
        var order = create_order();
        orderController.add_row(
            sessionToken,
            order.id,
            response,
            OrderController.OrderRowRequest(11, 10, listOf(3, 2, 1), 1)
        );
        order =
            orderController.getById(sessionToken, order.id, response).let { t -> t as ResultResponse<Order> }.load;
        println("Serialized order: ${order.asJson()}");
        assert(order.orderStatus == OrderStatus.READY.status);
        assert(order.userId == 10.toLong())
        assert(order.orderRows.size == 1);
    }

    @Test
    fun order_delete_row() {
        val response = MockHttpServletResponse();
        val sessionToken = "ciao";
        userAuthorizationService.onValidateUserToken = { userid: String ->
            UserToken<UserAccount>(
                200, UserAccount(
                    10,
                    "via san mazzari"
                )
            );
        }

        mockResponder.onOrderCreate = { order: Order ->

        }

        var order =
            orderController.create_order(sessionToken, response).let { t -> (t as ResultResponse<Order>).load };
        orderController.add_row(
            sessionToken,
            order.id,
            response,
            OrderController.OrderRowRequest(11, 10, listOf(3, 2, 1), 1)
        );
        orderController.remove_row(sessionToken, order.id, response, 1);
        order =
            orderController.getById(sessionToken, order.id, response).let { t -> t as ResultResponse<Order> }.load;
        println("Serialized order: ${order.asJson()}");
        assert(order.orderStatus == OrderStatus.READY.status);
        assert(order.userId == 10.toLong())
        assert(order.orderRows.isEmpty());
    }


    @Test
    fun test_order_submission() {
        var order = create_order();
        val response = MockHttpServletResponse();
        var sessionToken = "ciao";
        orderController.add_row(
            sessionToken,
            order.id,
            response,
            OrderController.OrderRowRequest(11, 10, listOf(3, 2, 1), 1)
        );
        order = orderController.submit_order(sessionToken, order.id, response)
            .let { t -> t as ResultResponse<Order> }.load;
        assert(order.orderStatus == OrderStatus.QUEUED.status);
    }


    @Test
    fun pizzeria_accept() {
        val response = MockHttpServletResponse();
        val sessionToken = "ciao";
        val pizzeriaid = "1002931"
        userAuthorizationService.onValidateManagerAccount = { userid: String ->
            UserToken<ManagerAccount>(
                200, ManagerAccount(
                    101,
                    "via meletti",
                    pizzeriaid
                )
            )
        }

        var order = create_order();
        order = orderController.accept_order(sessionToken, pizzeriaid, order.id, response)
            .let { t -> t as ResultResponse<Order> }.load;
        assert(order.pizzeriaId == pizzeriaid);
    }


}
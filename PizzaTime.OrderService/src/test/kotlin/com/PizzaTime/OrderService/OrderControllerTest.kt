package com.PizzaTime.OrderService

import BaseCommunicationService
import com.PizzaTime.OrderService.Messages.ResponseMessage
import com.PizzaTime.OrderService.Model.Order
import com.PizzaTime.OrderService.Model.OrderStatus
import com.PizzaTime.OrderService.Services.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.mock.web.MockHttpServletResponse

@SpringBootTest(
    properties = [
        "order.sagaservice.enabled=false",
        "order.userauthservice.enabled=false"
    ]
)
class OrderControllerTest {

    @Qualifier("mockUserService")
    @Autowired
    private lateinit var userAuthorizationService: MockUserService;

    @Qualifier("mockResponder")
    @Autowired
    private lateinit var communicationService: MockResponder;

    @Autowired
    private lateinit var orderController: OrderController

    @Test
    fun contextLoads() {

    }


    @Test
    fun order_creation(){
        val response = MockHttpServletResponse();
        userAuthorizationService.onValidateUserToken = {userid: String ->
            UserToken<UserAccount>(200,UserAccount(
               10,
                "via san mazzari"
            ));
        }

        val order = orderController.create_order("ciao", response).let { t-> (t as ResponseMessage<Order>).payload };
        assert(order.orderStatus == OrderStatus.READY.status);
        assert(order.userId == 10.toLong())
        assert(order.orderRows.isEmpty());
    }


    @Test
    fun order_add_row(){
        val response = MockHttpServletResponse();
        userAuthorizationService.onValidateUserToken = {userid: String ->
            UserToken<UserAccount>(200,UserAccount(
                10,
                "via san mazzari"
            ));
        }

        val order = orderController.create_order("ciao", response).let { t-> (t as ResponseMessage<Order>).payload };

    }

}
package com.PizzaTime.OrderService

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest(
    properties = [
        "order.sagaservice.enabled=false"
    ]
)
class OrderControllerTest {

    @Autowired
    private lateinit var orderController: OrderController


    @Test
    fun contextLoads() {

    }


    @Test
    fun order_creation(){
        val orderId = orderController.create_order("ciao");
        orderController.add_pizza("ciao",orderId,10);
        orderController.getById(orderId).let { t->
            t.pizzas.forEach { p-> println(p) };
            println(t.orderStatus)
            println(t.totalPrice)
            println(t.userId)
        }
    }

}
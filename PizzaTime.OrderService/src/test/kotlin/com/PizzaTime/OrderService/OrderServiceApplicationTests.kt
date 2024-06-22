package com.PizzaTime.OrderService

import com.PizzaTime.OrderService.Services.SagaNotifyService
import com.PizzaTime.OrderService.Services.OrderService
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.mock.web.MockHttpServletResponse

@SpringBootTest
class OrderServiceApplicationTests {

	@Autowired
	private lateinit var orderController: OrderController

	@Autowired
	private lateinit var communicationService : SagaNotifyService;

	@Autowired
	private lateinit var orderService: OrderService;

	@Test
	fun submitOrder() {
		val httpResponse = MockHttpServletResponse();


	}




}

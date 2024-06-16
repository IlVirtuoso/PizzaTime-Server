package com.PizzaTime.OrderService

import com.PizzaTime.OrderService.Services.CommunicationService
import com.PizzaTime.OrderService.Services.ICommunicationService
import com.PizzaTime.OrderService.Services.OrderService
import com.rabbitmq.client.ConnectionFactory
import jakarta.servlet.http.HttpServletResponse
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.server.ServletServerHttpResponse
import org.springframework.mock.web.MockHttpServletResponse

@SpringBootTest
class OrderServiceApplicationTests {

	@Autowired
	private lateinit var orderController: OrderController

	@Autowired
	private lateinit var communicationService : CommunicationService;

	@Autowired
	private lateinit var orderService: OrderService;

	@Test
	fun submitOrder() {
		val httpResponse = MockHttpServletResponse();
		orderController = OrderController(orderService,communicationService,httpResponse);

	}




}

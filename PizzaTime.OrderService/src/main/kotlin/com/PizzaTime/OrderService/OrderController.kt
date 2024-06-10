package com.PizzaTime.OrderService

import com.PizzaTime.OrderService.Services.OrderService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping

@Controller
class OrderController {

    @Autowired
    var orderRepository : OrderService? = null;


}
package com.PizzaTime.OrderService

import BaseCommunicationService
import org.springframework.core.env.Environment
import org.springframework.core.env.get

class MockResponder(val environment: Environment): BaseCommunicationService(
    environment.get("amqp.user")!!, environment.get("amqp.password")!!,environment.get("amqp.host")!!
) {


}
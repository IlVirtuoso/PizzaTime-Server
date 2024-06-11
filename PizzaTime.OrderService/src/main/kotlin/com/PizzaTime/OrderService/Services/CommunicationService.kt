package com.PizzaTime.OrderService.Services

import com.rabbitmq.client.Connection
import com.rabbitmq.client.ConnectionFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.env.Environment
import org.springframework.core.env.get
import org.springframework.stereotype.Service
import BaseCommunicationService;

@Service
class CommunicationService(final val environment: Environment) : BaseCommunicationService(environment.get("amqp.user")!!, environment.get("amqp.password")!!,environment.get("amqp.host")!!) {


    fun getUserFromToken(token: String): String {
        throw NotImplementedError();
    }
}
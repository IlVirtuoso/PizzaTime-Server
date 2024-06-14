package com.PizzaTime.OrderService.Services

import BaseCommunicationService
import com.rabbitmq.client.Connection
import com.rabbitmq.client.ConnectionFactory
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.coroutineScope
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.env.Environment
import org.springframework.core.env.get
import org.springframework.stereotype.Service
import java.util.concurrent.Future
import java.util.concurrent.FutureTask

@Service
class CommunicationService(final val environment: Environment) : BaseCommunicationService(environment.get("amqp.user")!!, environment.get("amqp.password")!!,environment.get("amqp.host")!!), ICommunicationService {
    override suspend fun getUserFromToken(token: String): Deferred<String> {
        TODO("Not yet implemented")
    }

    override suspend fun sumPizzasPrice(pizzas: List<Long>): Deferred<Double> {
        TODO("Not yet implemented")
    }

    override suspend fun isUserAdminForPizzeria(userToken: String, piva: String): Deferred<Boolean> {
        TODO("Not yet implemented")
    }

    override suspend fun searchAvailablePizzeriaForOrder(userId: String, pizzas: List<Long>): Deferred<String> {
        TODO("Not yet implemented")
    }




}
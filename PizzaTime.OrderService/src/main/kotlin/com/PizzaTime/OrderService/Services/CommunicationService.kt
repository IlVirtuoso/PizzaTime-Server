package com.PizzaTime.OrderService.Services

import BaseCommunicationService
import ExchangeType
import com.rabbitmq.client.AMQP.Queue
import com.rabbitmq.client.Connection
import com.rabbitmq.client.ConnectionFactory
import com.rabbitmq.client.RpcClient
import com.rabbitmq.client.RpcServer
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
class CommunicationService : BaseCommunicationService, ICommunicationService {
    object DECLARATION{
        const val user_exchange = "user_values";
        const val user_ids = "userid";
        const val user_admins = "useradmins";
    }




    constructor(environment: Environment) : super(
        environment.get("amqp.user")!!,
        environment.get("amqp.password")!!,
        environment.get("amqp.host")!!
    ) {
        channel.exchangeDeclare(DECLARATION.user_exchange,ExchangeType.DIRECT.type);
        channel.queueDeclare(DECLARATION.user_ids, true, false, false, null);
        channel.queueDeclare(DECLARATION.user_admins, true, false, false, null);
    }




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
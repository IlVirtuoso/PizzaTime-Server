package com.PizzaTime.OrderService.Services

import BaseCommunicationService
import ExchangeType
import com.PizzaTime.OrderService.Order
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
class CommunicationService(environment: Environment) : BaseCommunicationService(
    environment.get("amqp.user")!!,
    environment.get("amqp.password")!!,
    environment.get("amqp.host")!!
), ICommunicationService {
    companion object {
        const val saga_exchange = "OrderService/saga/json/requests";
        const val saga_send = "OrderService/json/send"
    }


    init {
        channel.exchangeDeclare(saga_exchange,ExchangeType.DIRECT.type)
        channel.queueDeclare("sagarequests", true, false, false, null)
    }

    override fun notifyOrderCreate(sessionToken: String, order: Order) {
        TODO("Not yet implemented")
    }

    override fun notifyOrderAccepted(order: Order) {
        TODO("Not yet implemented")
    }

    override fun notifyOrderServing(order: Order) {
        TODO("Not yet implemented")
    }


}
package com.PizzaTime.OrderService.Services.Amqp

import com.rabbitmq.client.Channel
import com.rabbitmq.client.ConnectionFactory
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.core.env.Environment
import org.springframework.core.env.get
import org.springframework.stereotype.Service

@Service
@ConditionalOnProperty(
    prefix = "PizzaOrders.Channel",
    value = ["enabled"],
    havingValue = "true",
    matchIfMissing = true
)
class AmqpChannelProvider(environment: Environment){

    final val channel : Channel;

    init {
        val userName = environment.get("amqp.username");
        val password = environment.get("amqp.password");
        val host = environment.get("amqp.host");
        channel = ConnectionFactory().let { t ->
            t.host = host; t.password = password; t.username = userName; return@let t;
        }.newConnection().createChannel();

    }


}
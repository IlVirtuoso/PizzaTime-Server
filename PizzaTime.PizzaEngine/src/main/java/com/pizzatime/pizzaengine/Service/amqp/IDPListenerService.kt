package com.pizzatime.pizzaengine.Service.amqp

import com.pizzatime.pizzaengine.Service.MenuService
import com.rabbitmq.client.AMQP
import com.rabbitmq.client.BuiltinExchangeType
import com.rabbitmq.client.DefaultConsumer
import com.rabbitmq.client.Envelope


class IDPListenerService(amqpChannelProvider: AmqpChannelProvider, val menuService: MenuService) :
    DefaultConsumer(amqpChannelProvider.channel) {

    companion object {
        const val exchange = "PizzaTime.IDP"
        const val onPizzeriaCreated = "IDP/OnPizzeriaCreated"
    }

    var queue = "";

    init {
        channel.exchangeDeclare(exchange, BuiltinExchangeType.DIRECT);
        queue = channel.queueDeclare().queue;
        channel.queueBind(queue, exchange, onPizzeriaCreated);
    }


    override fun handleDelivery(
        consumerTag: String?,
        envelope: Envelope?,
        properties: AMQP.BasicProperties?,
        body: ByteArray?,
    ) {
        when (envelope!!.routingKey) {
            onPizzeriaCreated -> {
                OnPizzeriaCreated(body!!, properties!!.type);
            }
        }
    }


    private fun OnPizzeriaCreated(data: ByteArray, messageType: String) {
        data class OnPizzeriaCreatedEventArgs(val pizzeriaId: Long);
        val response = fromJson<OnPizzeriaCreatedEventArgs>(data.decodeToString());
        menuService.createMenu(response.pizzeriaId);
    }
}
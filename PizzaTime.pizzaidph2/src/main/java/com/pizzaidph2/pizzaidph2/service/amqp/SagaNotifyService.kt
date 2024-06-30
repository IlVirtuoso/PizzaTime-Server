package com.pizzaidph2.pizzaidph2.service.amqp

import com.google.gson.Gson
import com.rabbitmq.client.AMQP.BasicProperties
import com.rabbitmq.client.BuiltinExchangeType
import org.springframework.stereotype.Service


interface ISagaNotifyService {
    fun notifyPizzeriaRegistration(pizzeriaId: Long);
}


@Service
class SagaNotifyService(val amqpChannelProvider: AmqpChannelProvider) : ISagaNotifyService {

    val channel = amqpChannelProvider.channel;
    companion object{
        const val pizzaengineexchange = "PizzaTime.IDP"
        const val onPizzeriaCreatedKey = "IDP/OnPizzeriaCreated"
    }

    init{
        channel.exchangeDeclare(pizzaengineexchange, BuiltinExchangeType.DIRECT);
    }

   override fun notifyPizzeriaRegistration(pizzeriaId: Long){
        val response = object{val pizzeriaId = pizzeriaId};
        channel.basicPublish(
            pizzaengineexchange,onPizzeriaCreatedKey,
            BasicProperties().builder().type("IDPNotification").build(),
            Gson().toJson(response).encodeToByteArray());
    }

}
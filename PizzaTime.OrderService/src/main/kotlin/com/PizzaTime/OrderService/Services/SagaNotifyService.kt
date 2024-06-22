package com.PizzaTime.OrderService.Services

import BaseCommunicationService
import ExchangeType
import com.PizzaTime.OrderService.Model.Order
import com.rabbitmq.client.*
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.core.env.Environment
import org.springframework.core.env.get
import org.springframework.stereotype.Service



class SagaListenerService(val channel: Channel): Consumer{

    companion object{
        const val saga_exchange = "PizzaTime/Saga"
        const val order_key = "OrderServiceRequest"
    }

    var queue: String = ""
    init {
        queue = channel.queueDeclare().queue
        channel.queueBind(queue, saga_exchange, order_key)
        channel.basicConsume(queue,this)
    }

    override fun handleConsumeOk(consumerTag: String?) {
        TODO("Not yet implemented")
    }

    override fun handleCancelOk(consumerTag: String?) {
        TODO("Not yet implemented")
    }

    override fun handleCancel(consumerTag: String?) {
        TODO("Not yet implemented")
    }

    override fun handleShutdownSignal(consumerTag: String?, sig: ShutdownSignalException?) {
        TODO("Not yet implemented")
    }

    override fun handleRecoverOk(consumerTag: String?) {
        TODO("Not yet implemented")
    }

    override fun handleDelivery(
        consumerTag: String?,
        envelope: Envelope?,
        properties: AMQP.BasicProperties?,
        body: ByteArray?
    ) {
        if(body == null){
            throw IllegalArgumentException("Saga messages cannot be null");
        }

    }

}


@ConditionalOnProperty(
    prefix = "order.sagaservice",
    value = ["enabled"],
    havingValue = "true",
    matchIfMissing = true
)
@Service
class SagaNotifyService(environment: Environment) : BaseCommunicationService(
    environment.get("amqp.user")!!,
    environment.get("amqp.password")!!,
    environment.get("amqp.host")!!
), ICommunicationService {
    companion object {
        const val saga_notification_exchange = "PizzaTime/Saga"
    }


    init {
        channel.exchangeDeclare(saga_notification_exchange,ExchangeType.DIRECT.type)
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
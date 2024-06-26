package com.PizzaTime.OrderService.Services

import BaseCommunicationService
import ExchangeType
import com.PizzaTime.OrderService.Model.Order
import com.PizzaTime.OrderService.Model.asJson
import com.rabbitmq.client.*
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.core.env.Environment
import org.springframework.core.env.get
import org.springframework.stereotype.Service


class SagaListenerService(val channel: Channel, orderService: OrderService): Consumer{

    companion object{
        const val saga_exchange = "PizzaTime.Order"
        const val order_key = "OrderRequests/Json"
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
        const val saga_notification_exchange = "PizzaTime.Order"
        const val created_order_key = "OrderEvents/Json/OrderCreated"
        const val order_changed = "OrderEvents/Json/OrderStatusChanged"
        const val order_canceled = "OrderEvents/Json/OrderCanceled"
    }


    init {
        channel.exchangeDeclare(saga_notification_exchange,ExchangeType.DIRECT.type)
    }

    override fun notifyOrderCreate( order: Order) {
        channel.basicPublish(
            saga_notification_exchange,
            created_order_key,
            AMQP.BasicProperties.Builder()
                .type("OrderCreationNotification")
                .contentType("application/json")
            .build(),
            order.asJson().encodeToByteArray()
        )
    }

    override fun notifyOrderStatusChanged(order: Order) {
        channel.basicPublish(
            saga_notification_exchange,
            order_changed,
            AMQP.BasicProperties().builder()
                .type("OrderStatusChangedNotification")
                .contentType("application/json")
                .build(),
            order.asJson().encodeToByteArray()
        )
    }

    override fun notifyOrderCancellation(order: Order) {
        channel.basicPublish(
            saga_notification_exchange,
            order_canceled,
            AMQP.BasicProperties().builder()
                .type("OrderCancellationNotification")
                .contentType("application/json")
                .build()
            ,order.asJson().encodeToByteArray()
        )
    }


}
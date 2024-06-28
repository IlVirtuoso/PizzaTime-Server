package com.PizzaTime.OrderService.Services.Amqp


import com.PizzaTime.OrderService.Model.Order
import com.PizzaTime.OrderService.Model.asJson
import com.PizzaTime.OrderService.Model.fromJson
import com.PizzaTime.OrderService.Services.IOrderService
import com.PizzaTime.OrderService.Services.OrderService
import com.rabbitmq.client.*
import kotlinx.coroutines.flow.channelFlow
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.core.env.Environment
import org.springframework.core.env.get
import org.springframework.stereotype.Service


@Service
class SagaListenerService( val orderService: IOrderService, val sagaNotifyService: SagaNotifyService, amqpChannelProvider: AmqpChannelProvider) : Consumer {

    companion object {
        const val saga_exchange = "PizzaTime.Order"
        const val order_key = "OrderRequests/Json"

    }

    data class GeneralResponse(var isError: Boolean, var payload: String);

    final val channel: Channel;
    final var queue: String = ""

    init {
        channel = amqpChannelProvider.channel;
        queue = channel.queueDeclare().queue
        channel.queueBind(queue, saga_exchange, order_key)
        channel.basicConsume(queue, false,this)
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
        body: ByteArray?,
    ) {

        if (body == null) {
            throw IllegalArgumentException("Saga messages cannot be null");
        }
        val response = fromJson<GeneralResponse>(body.decodeToString());



        when(properties!!.type){

            "OrderSubmissionResponse"-> {
                data class SubmissionReport(val orderId : String, val totalPrice : Double);
                val result = fromJson<SubmissionReport>(response.payload);
                orderService.finalizeOrderSubmission(result.orderId,result.totalPrice);
            }

            "BalanceReceivedNotification"->{
                data class BalanceReport(val orderId : String);
                val result = fromJson<BalanceReport>(response.payload);
                orderService.finalizeOrderServing(result.orderId);
            }
        }

        channel.basicAck(envelope!!.deliveryTag,false);
    }

}


@ConditionalOnProperty(
    prefix = "order.sagaservice",
    value = ["enabled"],
    havingValue = "true",
    matchIfMissing = true
)
@Service
class SagaNotifyService(environment: Environment) : ICommunicationService {
    companion object {
        const val saga_notification_exchange = "PizzaTime.Order"
        const val created_order_key = "OrderEvents/Json/OrderCreated"
        const val order_changed = "OrderEvents/Json/OrderStatusChanged"
        const val order_canceled = "OrderEvents/Json/OrderCanceled"

        private fun get_channel(environment: Environment): Channel {
            return ConnectionFactory().let { t ->
                t.host = environment.get("amqp.host");
                t.username = environment.get("amqp.username");
                t.password = environment.get("amqp.password");
                return@let t
            }.newConnection().createChannel();


        }

    }

    var channel : Channel = get_channel(environment);

    init {
        channel.exchangeDeclare(saga_notification_exchange, BuiltinExchangeType.DIRECT)
    }

    override fun notifyOrderCreate(order: Order) {
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
                .build(), order.asJson().encodeToByteArray()
        )
    }


}
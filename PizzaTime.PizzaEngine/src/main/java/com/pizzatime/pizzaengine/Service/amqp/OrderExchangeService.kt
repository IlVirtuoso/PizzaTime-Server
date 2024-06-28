package com.pizzatime.pizzaengine.Service.amqp

import com.pizzatime.pizzaengine.Component.OrderRows
import com.rabbitmq.client.AMQP
import com.rabbitmq.client.AMQP.BasicProperties
import com.rabbitmq.client.DefaultConsumer
import com.rabbitmq.client.Envelope
import org.apache.catalina.Engine
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.stereotype.Service
import java.util.*


//data class OrderRow(val pizzaId: Long, val baseId: Long, val additions: Set<Long>)
data class OrderRecord(
    val id: String,
    val totalPrice: Double,
    val userId: Long,
    val pizzeriaId: Long,
    val orderStatus: String,
    val orderRows: Set<OrderRows>,
)

data class EngineResponse(val isError: Boolean, val orderId : String, val payload: String)


@Service
@ConditionalOnProperty(
    prefix = "PizzaEngine.Channel",
    value = ["enabled"],
    havingValue = "true",
    matchIfMissing = true
)
class OrderExchangeCommunicator(val amqpChannelProvider: AmqpChannelProvider, val service: IOrderExchangeService) :
    DefaultConsumer(amqpChannelProvider.channel) {
    var queue: String = ""

    companion object {
        const val order_saga_exchange = "PizzaTime.Order"
        const val created_order_key = "OrderEvents/Json/OrderCreated"
        const val order_changed = "OrderEvents/Json/OrderStatusChanged"
        const val order_canceled = "OrderEvents/Json/OrderCanceled"

        const val responses = "OrderRequests/Json"
    }

    init {
        queue = channel.queueDeclare().queue
        channel.exchangeDeclarePassive(order_saga_exchange);
        channel.queueBind(queue, order_saga_exchange, created_order_key)
        channel.queueBind(queue, order_saga_exchange, order_canceled)
        channel.queueBind(queue, order_saga_exchange, order_changed)
        channel.basicConsume(queue, false, this);

    }

    override fun handleDelivery(
        consumerTag: String?,
        envelope: Envelope?,
        properties: AMQP.BasicProperties?,
        body: ByteArray?,
    ) {
        val type = properties!!.type;
        val routingKey = envelope!!.routingKey;
        val data = fromJson<OrderRecord>(body!!.decodeToString());
        when (routingKey) {
            created_order_key -> {

            }

            order_changed->{
                when(data.orderStatus){
                    "queued"->{

                        val result= manageSubmissionOperation(data);
                        channel.basicPublish(order_saga_exchange, responses,
                            BasicProperties().builder()
                                .type("OrderSubmissionResponse")
                                .contentType("application/json")
                                .build(),
                            result.asJson(false).encodeToByteArray()
                        );
                    }
                }
            }
        }
        channel.basicAck(envelope.deliveryTag, false);
    }


    fun manageSubmissionOperation(record: OrderRecord): EngineResponse{
        try{
            val setted = service.OnOrderSubmitted(record);
            return EngineResponse(false, setted.orderId, setted.asJson(false));
        }
        catch (ex: Exception){
            return EngineResponse(true, record.id, "Error while processing order submission, ${ex.message}")
        }
    }



}
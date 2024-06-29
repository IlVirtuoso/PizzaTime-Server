package com.pizzaidph2.pizzaidph2.service.amqp

import com.google.gson.Gson
import com.pizzaidph2.pizzaidph2.Component.GenericResponse
import com.pizzaidph2.pizzaidph2.service.IAccountService
import com.rabbitmq.client.AMQP
import com.rabbitmq.client.Channel
import com.rabbitmq.client.DefaultConsumer
import com.rabbitmq.client.Envelope
import org.springframework.stereotype.Service

@Service
class SagaListenerService(val amqpChannelProvider: AmqpChannelProvider, val accountService: IAccountService) : DefaultConsumer(amqpChannelProvider.channel) {

    private data class BalanceRequest(val userId : Long, val orderId: String, val cost: Double);
    private data class IDPResponse(val isError: Boolean, val orderId: String, val payload: String)

    val queue : String;
    companion object{
        const val user_exchange = "PizzaTime.IDP";
        const val sagarequestkey = "IDPServiceRequest"

        const val order_exchange = "PizzaTime.Order"
        const val order_requests = "OrderRequests/Json"
    }

    init {
        queue = channel.queueDeclare().queue
        channel.exchangeDeclarePassive(user_exchange);
        channel.queueBind(queue,user_exchange, sagarequestkey);
        channel.basicConsume(queue,false,this);
    }
    

    override fun handleDelivery(
        consumerTag: String?,
        envelope: Envelope?,
        properties: AMQP.BasicProperties?,
        body: ByteArray?
    ) {


        when(properties!!.type){
            "OrderSubmissionBalanceRequest"->{
                val data = Gson().fromJson(body!!.decodeToString(), BalanceRequest::class.java)
                manageCharging(data);
            }
        }


        channel.basicAck(envelope!!.deliveryTag, false);
    }


    private fun manageCharging(request: BalanceRequest){
        val result = Gson().fromJson(accountService.chargeOnBalance(request.userId, request.cost.toFloat()), GenericResponse::class.java);
        var response: IDPResponse? = null;
        when(result.statusCode){

            GenericResponse.NOT_ENOUGH_MONEY_CODE-> {
                response = IDPResponse(true, request.orderId, "NOT_ENOUGH_MONEY");
            }
            GenericResponse.GENERIC_ERROR_CODE->{
                response = IDPResponse(true, request.orderId,"GENERIC_ERROR");
            }
            GenericResponse.OK_CODE->{
                response = IDPResponse(false, request.orderId, "OK");
            }
        }
        channel.basicPublish(order_exchange, order_exchange,
            AMQP.BasicProperties().builder().type("BalanceReceivedNotification").build(),
            Gson().toJson(response!!).encodeToByteArray()
            )
    }
}
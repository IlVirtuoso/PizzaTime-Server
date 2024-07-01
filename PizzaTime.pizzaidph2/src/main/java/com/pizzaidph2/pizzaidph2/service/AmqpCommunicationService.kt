package com.pizzaidph2.pizzaidph2.service

import com.google.gson.Gson
import com.pizzaidph2.pizzaidph2.model.Pizzeria
import com.pizzaidph2.pizzaidph2.service.amqp.AmqpChannelProvider

import com.rabbitmq.client.*
import org.springframework.beans.factory.config.ConfigurableBeanFactory
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Scope
import org.springframework.core.env.Environment
import org.springframework.core.env.get
import org.springframework.stereotype.Service

private inline fun <reified T> T.asJson() : String = Gson().toJson(this)
private inline fun <reified T> T.fromJson(content: String): T = Gson().fromJson(content, T::class.java)

data class AmqpIdpResponse(var isError: Boolean, val payload: String)
data class AmqpIdpRequest(val token: String)
data class AccountRecord(val id : Long, val address: String);
data class ManagerRecord(val id : Long, val address: String, val pizzeria: Pizzeria);

@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
@ConditionalOnProperty(
    prefix = "idp.RpcChannel",
    value = ["enabled"],
    havingValue = "true",
    matchIfMissing = true
)
class AmqpCommunicationService(environment: Environment, var userService: AmqpUserService, amqpChannelProvider: AmqpChannelProvider) :
    RpcServer(amqpChannelProvider.channel) {

    companion object {
        const val user_exchange = "PizzaTime.IDP";
        const val request_routing_key = "IDPServiceRequest"
    }


    init {
        channel.exchangeDeclare(user_exchange, BuiltinExchangeType.DIRECT)
        channel.queueBind(queueName, user_exchange, request_routing_key);
        Thread{mainloop()}.start()
    }

   
    override fun handleCall(
        requestProperties: AMQP.BasicProperties?,
        requestBody: ByteArray?,
        replyProperties: AMQP.BasicProperties?
    ): ByteArray {
        val request = Gson().fromJson(requestBody?.decodeToString(), AmqpIdpRequest::class.java);

        if(requestProperties == null){
            return AmqpIdpResponse(true,"Parameters must be filled").asJson<AmqpIdpResponse>().encodeToByteArray();
        }

        when (requestProperties.type) {
            "VerifyUserTokenRequest" -> {
                val opt = userService.VerifyUserToken(request.token);
                if(opt.isPresent){
                    val info = AccountRecord(opt.get().id,opt.get().address);
                    return AmqpIdpResponse(false,info.asJson()).asJson().encodeToByteArray();
                }

            }
            "VerifyManagerTokenRequest"->{
                val opt = userService.VerifyManagerToken(request.token);
                if(opt.isPresent) {
                    val pizzeria = userService.GetPizzeriaForManagers(opt.get().id);
                    if (pizzeria.isPresent) {
                        val info = ManagerRecord(opt.get().id, opt.get().address, pizzeria.get());
                        return AmqpIdpResponse(false, info.asJson()).asJson().encodeToByteArray();
                    }
                }
            }
        }
        return AmqpIdpResponse(true,"Invalid Token").asJson<AmqpIdpResponse>().encodeToByteArray();
    }


}
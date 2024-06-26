package main.java.com.pizzaidph2.pizzaidph2.service

import com.google.gson.Gson
import com.pizzaidph2.pizzaidph2.model.Account
import com.pizzaidph2.pizzaidph2.service.AmqpUserService
import com.rabbitmq.client.*
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.core.env.Environment
import org.springframework.core.env.get
import org.springframework.stereotype.Service
import java.util.*

private inline fun <reified T> T.asJson() : String = Gson().toJson(this)
private inline fun <reified T> T.fromJson(content: String): T = Gson().fromJson(content, T::class.java)

data class AmqpIdpResponse(var isError: Boolean, val payload: String)
data class AmqpIdpRequest(val token: String)
data class AccountRecord(val id : Long, val address: String, val vatNumber : String);

@Service
@ConditionalOnProperty(
    prefix = "idp.RpcChannel",
    value = ["enabled"],
    havingValue = "true",
    matchIfMissing = true
)
class AmqpCommunicationService(environment: Environment, var userService: AmqpUserService) :
    RpcServer(build(environment)) {

    companion object {
        const val user_exchange = "PizzaTime.IDP";
        const val request_routing_key = "IDPServiceRequest"

        fun build(environment: Environment): Channel {
            val userName = environment.get("amqp.user");
            val password = environment.get("amqp.password");
            val host = environment.get("amqp.host");
            val channel = ConnectionFactory().let { t ->
                t.host = host; t.password = password; t.username = userName; return@let t;
            }.newConnection().createChannel();
            return channel;
        }
    }


    init {
        channel.exchangeDeclare(user_exchange, BuiltinExchangeType.DIRECT)
        channel.queueBind(queueName, user_exchange, request_routing_key);
        mainloop();
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
                    val info = AccountRecord(opt.get().id,opt.get().address,"");
                    return AmqpIdpResponse(false,info.asJson()).asJson().encodeToByteArray();
                }

            }
            "VerifyManagerTokenRequest"->{
                val opt = userService.VerifyManagerToken(request.token);
                if(opt.isPresent){
                    val vat = userService.GetVatForManager(opt.get().id).get();
                    val info = AccountRecord(opt.get().id,opt.get().address,vat);
                    return AmqpIdpResponse(false,info.asJson()).asJson().encodeToByteArray();
                }
            }
        }
        return AmqpIdpResponse(true,"Invalid Token").asJson<AmqpIdpResponse>().encodeToByteArray();
    }


}
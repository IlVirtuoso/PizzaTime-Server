package main.java.com.pizzaidph2.pizzaidph2.service

import com.google.gson.Gson
import com.pizzaidph2.pizzaidph2.model.Account
import com.pizzaidph2.pizzaidph2.service.AmqpUserService
import com.rabbitmq.client.*
import org.springframework.core.env.Environment
import org.springframework.core.env.get
import org.springframework.stereotype.Service

data class AmqpIdpResponse(var isError: Boolean, val payload: String)
data class AmqpIdpRequest(val token: String);
@Service
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
        when (requestProperties!!.type) {
            "VerifyUserTokenRequest" -> {
                val opt = userService.VerifyUserToken(request.token);
                if (opt.isEmpty) {
                    return Gson().toJson(AmqpIdpResponse(true, "Invalid Token")).encodeToByteArray();
                }
                return Gson().toJson(AmqpIdpResponse(false, opt.get().jsonfy())).encodeToByteArray();
            }
        }
        return "ERROR".encodeToByteArray();
    }


}
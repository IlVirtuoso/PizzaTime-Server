package com.pizzatime.pizzaengine.Service.amqp

import com.google.gson.Gson
import com.rabbitmq.client.AMQP
import com.rabbitmq.client.ConnectionFactory
import com.rabbitmq.client.RpcClient
import com.rabbitmq.client.RpcClientParams
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.core.env.Environment
import org.springframework.core.env.get
import org.springframework.stereotype.Service
import java.util.*

class Pizzeria{
    var id : Long = -1;
}

data class IdpMessage(var isError: Boolean, var payload: String)
data class IdpRequest(var token: String)
data class UserAccount(var id: Long, var address: String)
data class ManagerAccount(var id : Long, var address: String, var pizzeria: Pizzeria)





@Service
@ConditionalOnProperty(
    prefix = "PizzaEngine.UserAuthService",
    value = ["enabled"],
    havingValue = "true",
    matchIfMissing = true
)
class UserAuthorizationService(val amqpChannelProvider: AmqpChannelProvider) : RpcClient(get_client_config(amqpChannelProvider)),
    IUserAuthorizationService {

    companion object{

        const val exchange = "PizzaTime.IDP"
        const val routingKey = "IDPServiceRequest"
        private fun get_client_config(amqpChannelProvider: AmqpChannelProvider) : RpcClientParams{
            return RpcClientParams().let {
                    t->
                t.channel(amqpChannelProvider.channel).exchange(exchange).routingKey(routingKey)
                return@let t
            }
        }
    }


    override fun validateUserIdToken(token: String): Optional<UserAccount> {
        val result = primitiveCall(
            AMQP.BasicProperties().builder().type("VerifyUserTokenRequest").build(),
            Gson().toJson(IdpRequest(token)).encodeToByteArray()
        );
        val message = Gson().fromJson(result.decodeToString(), IdpMessage::class.java);
        if(message.isError){
            return Optional.empty();
        }
        return Optional.of(fromJson<UserAccount>(message.payload))
    }

    override fun validateManagerIdToken(token: String): Optional<ManagerAccount>{
        val result = primitiveCall(AMQP.BasicProperties().builder().type("VerifyManagerTokenRequest").build(),
            Gson().toJson(IdpRequest(token)).encodeToByteArray()
        );
        val message = Gson().fromJson(result.decodeToString(), IdpMessage::class.java);
        if(message.isError){
            return Optional.empty();
        }
        return Optional.of(fromJson<ManagerAccount>(message.payload))
    }

}
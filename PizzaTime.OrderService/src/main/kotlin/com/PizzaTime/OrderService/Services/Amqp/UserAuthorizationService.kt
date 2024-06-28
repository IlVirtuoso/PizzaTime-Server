package com.PizzaTime.OrderService.Services.Amqp


import com.PizzaTime.OrderService.Model.asJson
import com.PizzaTime.OrderService.Model.fromJson
import com.google.gson.Gson
import com.rabbitmq.client.*
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.core.env.Environment
import org.springframework.core.env.get
import org.springframework.stereotype.Service
import java.util.*


data class Pizzeria(var id : Long, var address: String)

data class IdpMessage(var isError: Boolean, var payload: String)
data class IdpRequest(var token: String)

data class UserAccount(var id: Long, var address: String)

data class ManagerAccount(var id : Long, var address: String, var pizzeria: Pizzeria)



interface IUserAuthorizationService {
    fun validateUserIdToken(token: String): Optional<UserAccount>; //should be the deserialized version of the token
    fun validateManagerIdToken(token: String): Optional<ManagerAccount>;
}

@ConditionalOnProperty(
    prefix = "order.userauthservice",
    value = ["enabled"],
    havingValue = "true",
    matchIfMissing = true
)
@Service
class UserAuthorizationService(amqpChannelProvider: AmqpChannelProvider) :  RpcClient(get_client_config(amqpChannelProvider)) ,
    IUserAuthorizationService {

    companion object{

        const val exchange = "PizzaTime.IDP"
        const val routingKey = "IDPServiceRequest"
        private fun get_client_config(amqpChannelProvider: AmqpChannelProvider) : RpcClientParams{
            return RpcClientParams().channel(amqpChannelProvider.channel).exchange(exchange).routingKey(routingKey)
        }
    }

    init {

    }

    override fun validateUserIdToken(token: String): Optional<UserAccount> {
        var request = object{var token = token}.asJson(false);
        val result = primitiveCall(AMQP.BasicProperties().builder().type("VerifyUserTokenRequest").build(),
            request.encodeToByteArray()
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
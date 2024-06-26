package com.PizzaTime.OrderService.Services


import com.PizzaTime.OrderService.Model.fromJson
import com.google.gson.Gson
import com.rabbitmq.client.*
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.core.env.Environment
import org.springframework.core.env.get
import org.springframework.stereotype.Service
import java.util.*


data class IdpMessage(var isError: Boolean, var payload: String)
data class IdpRequest(var token: String)

data class UserAccount(var id: Long, var address: String)

data class ManagerAccount(var id : Long, var address: String, var vat: String)



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
class UserAuthorizationService(environment: Environment) :  RpcClient(get_client_config(environment)) ,IUserAuthorizationService {

    companion object{

        const val exchange = "PizzaTime.IDP"
        const val routingKey = "IPDServiceRequest"
        private fun get_client_config(environment: Environment) : RpcClientParams{
            val channel = ConnectionFactory().let { t->
                t.host = environment.get("amqp.host");
                t.username = environment.get("amqp.username");
                t.password = environment.get("amqp.password");
                return@let t
            }.newConnection().createChannel();

            RpcClientParams().let {
                t->
                t.channel(channel).exchange("PizzaTime.IDP").routingKey("IDPServiceRequest")
                return t
            }
        }
    }


    override fun validateUserIdToken(token: String): Optional<UserAccount> {
        val result = primitiveCall(AMQP.BasicProperties().builder().type("VerifyUserTokenRequest").build(),
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
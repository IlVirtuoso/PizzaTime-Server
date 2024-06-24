package com.PizzaTime.OrderService.Services

import BaseCommunicationService
import com.rabbitmq.client.Address
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.core.env.Environment
import org.springframework.core.env.get
import org.springframework.stereotype.Service



data class UserAccount(var id: Long, var address: String)

data class ManagerAccount(var id : Long, var address: String, var piva: String)

data class UserToken<T>(val statusCode: Int,var account: T?);


interface IUserAuthorizationService {
    fun validateUserIdToken(token: String): UserToken<UserAccount>; //should be the deserialized version of the token
    fun validateManagerIdToken(token: String): UserToken<ManagerAccount>;

}

@ConditionalOnProperty(
    prefix = "order.userauthservice",
    value = ["enabled"],
    havingValue = "true",
    matchIfMissing = true
)
@Service
class UserAuthorizationService(environment: Environment) : BaseCommunicationService(
    environment.get("amqp.user")!!,
    environment.get("amqp.password")!!,
    environment.get("amqp.host")!!
) , IUserAuthorizationService {
    override fun validateUserIdToken(token: String): UserToken<UserAccount> {
        TODO("Not yet implemented")
    }

    override fun validateManagerIdToken(token: String): UserToken<ManagerAccount> {
        TODO("Not yet implemented")
    }


}
package com.PizzaTime.OrderService.Services

import BaseCommunicationService
import org.springframework.core.env.Environment
import org.springframework.core.env.get
import org.springframework.stereotype.Service




interface IUserAuthorizationService {
    fun validateUserIdToken(token: String): String; //should be the deserialized version of the token

}

@Service
class UserAuthorizationService(environment: Environment) : BaseCommunicationService(
    environment.get("amqp.user")!!,
    environment.get("amqp.password")!!,
    environment.get("amqp.host")!!
) , IUserAuthorizationService {

    override fun validateUserIdToken(token: String):  String{
        TODO("Not yet implemented")
    }

}
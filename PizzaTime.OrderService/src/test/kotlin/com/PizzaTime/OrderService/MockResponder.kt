package com.PizzaTime.OrderService

import com.PizzaTime.OrderService.Model.Order
import com.PizzaTime.OrderService.Services.*
import org.springframework.core.env.Environment
import org.springframework.stereotype.Service


@Service
class MockResponder(val environment: Environment): ICommunicationService{

    var onOrderCreate : ((userid: String, order: Order)-> Unit)? = null;
    var onOrderAccepted : ((order: Order)-> Unit)? = null;
    var onOrderServing : ((order: Order)-> Unit)? = null;


    override fun notifyOrderCreate(sessionToken: String, order: Order) {
        onOrderCreate?.invoke(sessionToken, order)
    }

    override fun notifyOrderAccepted(order: Order) {
        onOrderAccepted?.invoke(order);
    }

    override fun notifyOrderServing(order: Order) {
        onOrderServing?.invoke(order);
    }
}

@Service
class MockUserService: IUserAuthorizationService{

    var onValidateUserToken : ((userid: String)-> UserToken<UserAccount>)? = null;
    var onValidateManagerAccount : ((userid: String) -> UserToken<ManagerAccount>)? = null;


    override fun validateUserIdToken(token: String): UserToken<UserAccount> {
        if(onValidateUserToken == null){
            UserToken<UserAccount>(500,null);
        }
        return onValidateUserToken?.invoke(token)!!
    }

    override fun validateManagerIdToken(token: String): UserToken<ManagerAccount> {
        if(onValidateUserToken == null){
            UserToken<ManagerAccount>(500,null);
        }
        return onValidateManagerAccount?.invoke(token)!!
    }

}
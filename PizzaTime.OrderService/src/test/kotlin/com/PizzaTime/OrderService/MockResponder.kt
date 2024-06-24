package com.PizzaTime.OrderService

import com.PizzaTime.OrderService.Model.Order
import com.PizzaTime.OrderService.Services.*
import org.springframework.core.env.Environment
import org.springframework.stereotype.Service


@Service
class MockResponder(val environment: Environment): ICommunicationService{

    var onOrderCreate : ((order: Order)-> Unit)? = null;
    var onOrderStatusChanged : ((order: Order)-> Unit)? = null;


    override fun notifyOrderCreate(order: Order) {
        onOrderCreate?.invoke(order)
    }

    override fun notifyOrderStatusChanged(order: Order) {
        onOrderStatusChanged?.invoke(order);
    }

    override fun notifyOrderCancellation(order: Order) {
        TODO("Not yet implemented")
    }

}


@Service
class MockUserService: IUserAuthorizationService{

    var onValidateUserToken : ((userid: String)-> UserToken<UserAccount>)? = null;
    var onValidateManagerAccount : ((userid: String) -> UserToken<ManagerAccount>)? = null;
    override fun validateUserIdToken(token: String): UserToken<UserAccount> {
       return onValidateUserToken!!.invoke(token);
    }

    override fun validateManagerIdToken(token: String): UserToken<ManagerAccount> {
        return onValidateManagerAccount!!.invoke(token);
    }


}
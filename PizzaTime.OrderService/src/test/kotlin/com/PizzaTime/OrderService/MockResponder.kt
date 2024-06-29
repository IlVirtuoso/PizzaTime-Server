package com.PizzaTime.OrderService

import com.PizzaTime.OrderService.Model.Order
import com.PizzaTime.OrderService.Services.Amqp.ICommunicationService
import com.PizzaTime.OrderService.Services.Amqp.IUserAuthorizationService
import com.PizzaTime.OrderService.Services.Amqp.ManagerAccount
import com.PizzaTime.OrderService.Services.Amqp.UserAccount
import org.springframework.core.env.Environment
import org.springframework.stereotype.Service
import java.util.*


@Service
class MockResponder(val environment: Environment): ICommunicationService {

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
class MockUserService: IUserAuthorizationService {

    var onValidateUserToken : ((userid: String)-> Optional<UserAccount>)? = null;
    var onValidateManagerAccount : ((userid: String) -> Optional<ManagerAccount>)? = null;
    override fun validateUserIdToken(token: String): Optional<UserAccount> {
       return onValidateUserToken!!.invoke(token);
    }

    override fun validateManagerIdToken(token: String): Optional<ManagerAccount> {
        return onValidateManagerAccount!!.invoke(token);
    }


}
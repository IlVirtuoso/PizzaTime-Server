package com.PizzaTime.OrderService

import BaseCommunicationService
import com.PizzaTime.OrderService.Services.ICommunicationService
import org.mockito.Mock
import org.springframework.context.annotation.Primary
import org.springframework.core.env.Environment
import org.springframework.core.env.get
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
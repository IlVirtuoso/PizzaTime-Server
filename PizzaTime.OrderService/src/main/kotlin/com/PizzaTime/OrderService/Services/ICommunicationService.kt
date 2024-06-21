package com.PizzaTime.OrderService.Services

import com.PizzaTime.OrderService.Order
import kotlinx.coroutines.Deferred
import java.util.concurrent.Future

interface ICommunicationService {
    fun notifyOrderCreate(userId: String, order: Order);
    fun notifyOrderAccepted(order: Order);
    fun notifyOrderServing(order: Order);
}
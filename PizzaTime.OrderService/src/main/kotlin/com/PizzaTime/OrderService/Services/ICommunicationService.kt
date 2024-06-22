package com.PizzaTime.OrderService.Services

import com.PizzaTime.OrderService.Model.Order

interface ICommunicationService {
    fun notifyOrderCreate(userId: String, order: Order);
    fun notifyOrderAccepted(order: Order);
    fun notifyOrderServing(order: Order);
}
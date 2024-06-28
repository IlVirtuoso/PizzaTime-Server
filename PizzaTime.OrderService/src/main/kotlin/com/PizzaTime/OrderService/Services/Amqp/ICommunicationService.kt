package com.PizzaTime.OrderService.Services.Amqp

import com.PizzaTime.OrderService.Model.Order

interface ICommunicationService {
    fun notifyOrderCreate(order: Order);
    fun notifyOrderStatusChanged(order: Order);
    fun notifyOrderCancellation(order: Order);
}
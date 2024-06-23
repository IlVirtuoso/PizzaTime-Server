package com.PizzaTime.OrderService.Services

import com.PizzaTime.OrderService.Model.Order

interface ICommunicationService {
    fun notifyOrderCreate(order: Order);
    fun notifyOrderStatusChanged(order: Order);
    fun notifyOrderCancellation(order: Order);
}
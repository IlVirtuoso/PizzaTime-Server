package com.PizzaTime.OrderService.Services

import com.PizzaTime.OrderService.Order

interface IOrderService
{
    fun getOrderById(id : String) : Order;
    fun save(order : Order);
    fun delete(order: Order);
    fun deleteOrderId(id : String);
    fun getPizzeriaFromId(id : String);
}
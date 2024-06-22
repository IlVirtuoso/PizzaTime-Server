package com.PizzaTime.OrderService.Services

import com.PizzaTime.OrderService.Model.Order
import com.PizzaTime.OrderService.Model.OrderStatus

interface IOrderService
{
    fun getOrderById(id : String) : Order;
    fun save(order : Order) : Order;
    fun delete(order: Order);
    fun deleteOrderId(id : String);
    fun getOrdersForPizzeria(pizzeriaId : String, orderStatus: OrderStatus) : Collection<Order>;
}
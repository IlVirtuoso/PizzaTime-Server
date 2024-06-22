package com.PizzaTime.OrderService.Services

import com.PizzaTime.OrderService.Model.Order
import com.PizzaTime.OrderService.Model.OrderRow
import com.PizzaTime.OrderService.Model.OrderStatus
import java.util.Optional

interface IOrderService
{
    fun getOrderById(id : String) : Optional<Order>;
    fun save(order : Order) : Order;
    fun delete(order: Order);
    fun deleteOrderId(id : String);
    fun getOrdersForPizzeria(pizzeriaId : String, orderStatus: OrderStatus) : Collection<Order>;
    fun saveRow(orderRow: OrderRow) : OrderRow;
    fun findRowById(orderId : String, id : Long) : Optional<OrderRow>;
    fun deleteOrderRow(orderId: String, lineId : Long);
}
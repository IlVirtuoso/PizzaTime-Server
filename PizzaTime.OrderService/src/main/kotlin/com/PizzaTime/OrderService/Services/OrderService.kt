package com.PizzaTime.OrderService.Services

import com.PizzaTime.OrderService.IOrderRepository
import com.PizzaTime.OrderService.Order
import org.springframework.stereotype.Service

@Service
class OrderService(private val orderRepository: IOrderRepository) {
    fun getOrderById(id : Long) : Order{
        return orderRepository.getReferenceById(id);
    }
}
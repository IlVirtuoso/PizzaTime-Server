package com.PizzaTime.OrderService.Services

import com.PizzaTime.OrderService.IOrderRepository
import com.PizzaTime.OrderService.Order
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.PostMapping

@Service
class OrderService(private val orderRepository: IOrderRepository) {

    fun getOrderById(id : String) : Order{
        return orderRepository.getReferenceById(id);
    }

    fun save(order : Order){
        orderRepository.save(order);
    }

    fun delete(order: Order){
        orderRepository.delete(order)
    }

    fun deleteOrderId(id : String){
        orderRepository.deleteById(id)
    }





}
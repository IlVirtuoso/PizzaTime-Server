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



    @PostMapping("api/v1/order/submit")
    fun submitOrder(){

    }



}
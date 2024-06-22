package com.PizzaTime.OrderService.Services

import com.PizzaTime.OrderService.IOrderRepository
import com.PizzaTime.OrderService.Model.Order
import com.PizzaTime.OrderService.Model.OrderStatus
import org.springframework.stereotype.Service

@Service
class OrderService(private val orderRepository: IOrderRepository) : IOrderService{

    override fun getOrderById(id : String) : Order {
        return orderRepository.getReferenceById(id);
    }

    override fun save(order : Order) : Order {
        return orderRepository.save(order);
    }

    override fun delete(order: Order){
        orderRepository.delete(order)
    }

    override fun deleteOrderId(id : String){
        orderRepository.deleteById(id)
    }


    override fun getOrdersForPizzeria(pizzeriaId: String, orderStatus: OrderStatus): Collection<Order> {
        return orderRepository.findAllOrdersForPizzeria(pizzeriaId,orderStatus.status);
    }





}
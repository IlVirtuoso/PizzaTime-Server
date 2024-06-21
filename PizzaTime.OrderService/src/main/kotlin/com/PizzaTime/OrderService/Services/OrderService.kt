package com.PizzaTime.OrderService.Services

import com.PizzaTime.OrderService.IOrderRepository
import com.PizzaTime.OrderService.Order
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.PostMapping

@Service
class OrderService(private val orderRepository: IOrderRepository) : IOrderService{

    override fun getOrderById(id : String) : Order{
        return orderRepository.getReferenceById(id);
    }

    override fun save(order : Order) : Order{
        return orderRepository.save(order);
    }

    override fun delete(order: Order){
        orderRepository.delete(order)
    }

    override fun deleteOrderId(id : String){
        orderRepository.deleteById(id)
    }

    override fun getPizzeriaOrders(pizzeriaId: String): Collection<Order> {
        return orderRepository.findAllOrdersForPizzeria(pizzeriaId);
    }


}
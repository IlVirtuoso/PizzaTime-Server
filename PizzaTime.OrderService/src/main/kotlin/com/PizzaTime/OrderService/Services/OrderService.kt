package com.PizzaTime.OrderService.Services

import com.PizzaTime.OrderService.Repositories.IOrderRepository
import com.PizzaTime.OrderService.Model.Order
import com.PizzaTime.OrderService.Model.OrderRow
import com.PizzaTime.OrderService.Model.OrderStatus
import com.PizzaTime.OrderService.Repositories.IOrderRowRepository
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import java.util.*

@Service
class OrderService(private val orderRepository: IOrderRepository, private val rowRepo: IOrderRowRepository) :
    IOrderService {
    @Transactional
    override fun getOrderById(id: String): Optional<Order> {
        return orderRepository.findById(id);
    }

    @Transactional
    override fun save(order: Order): Order {
        return orderRepository.save(order);
    }

    @Transactional
    override fun delete(order: Order) {
        orderRepository.delete(order)
    }

    @Transactional
    override fun deleteOrderId(id: String) {
        orderRepository.deleteById(id)
    }

    @Transactional
    override fun getOrdersForPizzeria(pizzeriaId: String, orderStatus: OrderStatus): Collection<Order> {
        return orderRepository.findAllOrdersForPizzeria(pizzeriaId, orderStatus.status);
    }

    @Transactional
    override fun saveRow(orderRow: OrderRow): OrderRow {
        return rowRepo.save(orderRow);
    }


    @Transactional
    override fun findRowById(orderId: String, id: Long): Optional<OrderRow> {
        return rowRepo.findById(id);
    }

    @Transactional
    override fun deleteOrderRow(orderId: String, lineId: Long) {
        rowRepo.deleteOrderRowById(orderId, lineId);
    }


}
package com.PizzaTime.OrderService.Services

import com.PizzaTime.OrderService.Repositories.IOrderRepository
import com.PizzaTime.OrderService.Model.Order
import com.PizzaTime.OrderService.Model.OrderRow
import com.PizzaTime.OrderService.Model.OrderStatus
import com.PizzaTime.OrderService.Repositories.IOrderRowRepository
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import java.util.*
import kotlin.collections.HashSet

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
    override fun getOrdersForPizzeria(pizzeriaId: String): Set<Order> {
        return orderRepository.findAllOrdersForPizzeria(pizzeriaId);
    }

    @Transactional
    override fun getPizzeriaHistory(pizzeriaId: String): Set<Order> {
        return orderRepository.getPizzeriaHistory(pizzeriaId);
    }

    @Transactional
    override fun saveRow(order: Order, orderRow: OrderRow): OrderRow {
        var row = orderRow;
        row = rowRepo.save(row)
        order.orderRows = HashSet(order.orderRows) + row
        orderRepository.save(order);
        return row;
    }


    @Transactional
    override fun findRowById(orderId: String, id: Long): Optional<OrderRow> {
        return rowRepo.findById(id);
    }

    @Transactional
    override fun deleteOrderRow(order: Order, orderRow: OrderRow) {
        order.orderRows -= orderRow;
        save(order);
        rowRepo.deleteById(orderRow.id!!);
    }

    @Transactional
    override fun finalizeOrderSubmission(orderId: String, price: Double) {
        TODO()
    }

    override fun finalizeOrderServing(orderId: String) {
        TODO("Not yet implemented")
    }


}
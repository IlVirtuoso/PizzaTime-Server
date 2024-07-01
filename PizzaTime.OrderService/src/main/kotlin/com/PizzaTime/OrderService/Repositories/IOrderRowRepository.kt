package com.PizzaTime.OrderService.Repositories

import com.PizzaTime.OrderService.Model.OrderRow
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface IOrderRowRepository : JpaRepository<OrderRow, Long> {



    @Query("select p from OrderRow p where p.order.id = :orderId")
    fun findAllByOrderId(orderId: Long): List<OrderRow>;

    @Query("delete from OrderRow p where p.order.id = :orderId and p.id = :lineId")
    fun deleteOrderRowById(orderId: String, lineId: Long);
}
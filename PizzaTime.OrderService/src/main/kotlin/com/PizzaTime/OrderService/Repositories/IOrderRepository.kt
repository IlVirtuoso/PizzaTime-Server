package com.PizzaTime.OrderService.Repositories


import com.PizzaTime.OrderService.Model.Order
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface IOrderRepository : JpaRepository<Order, String> {

    @Query("select p from Order p where p.pizzeriaId =: pizzeriaId and p.orderStatus != 'completed' and p.orderStatus != 'canceled'")
    fun findAllOrdersForPizzeria(pizzeriaId: String): Set<Order>;

    @Query("select p from Order p where p.pizzeriaId = :pizzeriaId")
    fun getPizzeriaHistory(pizzeriaId: String): Set<Order>;

}


package com.PizzaTime.OrderService.Repositories


import com.PizzaTime.OrderService.Model.Order
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface IOrderRepository : JpaRepository<Order, String> {

    @Query("select p from Order p where p.pizzeriaId =: pizzeriaId and p.orderStatus =: orderStatus")
    fun findAllOrdersForPizzeria(pizzeriaId: String, orderStatus: String): Collection<Order>;


}


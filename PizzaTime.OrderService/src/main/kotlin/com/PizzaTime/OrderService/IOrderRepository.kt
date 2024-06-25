package com.PizzaTime.OrderService


import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface IOrderRepository : JpaRepository<Order, String> {

    @Query("select p from Order p where p.pizzeriaId =: pizzeriaId")
    fun findAllOrdersForPizzeria(pizzeriaId : String): Collection<Order>;
}

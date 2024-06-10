package com.PizzaTime.OrderService


import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface IOrderRepository : JpaRepository<Order, Long> {
}


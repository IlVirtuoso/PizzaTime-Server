package com.PizzaTime.OrderService.Model

import jakarta.persistence.*

@Entity
class OrderRow {
    @Id
    @ManyToOne
    lateinit var order: Order;

    var pizzaId: Long = -1;
    var baseId : Long = -1;
    @ElementCollection
    @CollectionTable(name = "pizza_ingredients")
    var ingredients: Set<Long> = HashSet();
}
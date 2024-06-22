package com.PizzaTime.OrderService.Model

import jakarta.persistence.*

@Entity
class OrderRow {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    @ManyToOne
    lateinit var order: Order;
    var pizzaId: Long = -1;
    var baseId : Long = -1;
    @ElementCollection
    @CollectionTable(name = "pizza_ingredients")
    var ingredients: Set<Long> = HashSet();
    var quantity : Int = 0;
}
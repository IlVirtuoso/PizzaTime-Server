package com.PizzaTime.OrderService.Model

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.annotations.Expose
import jakarta.persistence.*

@Entity
class OrderRow{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    @ManyToOne
    lateinit var order: Order;
    @Expose
    var pizzaId: Long = -1;
    @Expose
    var baseId : Long = -1;
    @Expose
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "pizza_ingredients")
    var ingredients: Set<Long> = HashSet();
    @Expose
    var quantity : Int = 0;

}
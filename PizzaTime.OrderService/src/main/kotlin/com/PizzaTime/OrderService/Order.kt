package com.PizzaTime.OrderService

import jakarta.persistence.*
import java.time.Instant
import java.util.*


enum class OrderStatus {
    QUEUED ,
    SERVING,
    SERVED ,
    CANCELED
}


@Entity
@Table(name = "pizza_order")
class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    var id : String = "";
    var totalPrice: Double = 0.0;
    var date : Date = Date.from(Instant.now());
    var orderStatus: OrderStatus = OrderStatus.QUEUED;

    @ElementCollection
    var pizzas : List<Long>? = null;

}


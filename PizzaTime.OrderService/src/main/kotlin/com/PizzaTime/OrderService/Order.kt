package com.PizzaTime.OrderService

import jakarta.persistence.*
import java.time.Instant
import java.util.*


enum class OrderStatus {
    PENDING_AUTHORIZATION,
    READY,
    QUEUED,
    REFUSED,
    ACCEPTED,
    SERVING,
    COMPLETED ,
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
    var orderStatus: OrderStatus = OrderStatus.PENDING_AUTHORIZATION;
    var userId : String = "";
    var pizzeriaId : String = ""


    @ElementCollection
    var pizzas : List<Long>? = null;

}


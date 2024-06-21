package com.PizzaTime.OrderService

import jakarta.persistence.*
import java.time.Instant
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashSet
import kotlin.reflect.KClass


enum class OrderStatus(val status: String) {
    READY("ready"),
    QUEUED("queued"),
    REFUSED("refused"),
    ACCEPTED("accepted"),
    SERVING("serving"),
    COMPLETED("completed") ,
    CANCELED("canceled")
}


@Entity
@Table(name = "pizza_order")
class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    var id : String = "";
    var totalPrice: Double = 0.0;
    var date : Date = Date.from(Instant.now());
    var orderStatus: String = OrderStatus.READY.status;
    var userId : String = "";
    var pizzeriaId : String = ""

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "order_pizza", joinColumns = [JoinColumn(name = "order_id")])
    var pizzas : Set<Long> = HashSet<Long>();


}




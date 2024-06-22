package com.PizzaTime.OrderService.Model

import jakarta.persistence.*
import java.time.Instant
import java.util.*


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
    var userId : Long = -1;
    var pizzeriaId : String = ""

    @OneToMany(fetch = FetchType.EAGER)
    var orderRows: Set<OrderRow> = HashSet();
}




package com.PizzaTime.OrderService.Model

import com.google.gson.annotations.Expose
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
class Order : IJsonSerializable {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Expose
    var id : String = "";
    @Expose
    var totalPrice: Double = 0.0;
    @Expose
    var date : Date = Date.from(Instant.now());
    @Expose
    var orderStatus: String = OrderStatus.READY.status;
    @Expose
    var userId : Long = -1;
    @Expose
    var pizzeriaId : String = ""

    @Expose
    @OneToMany(fetch = FetchType.EAGER)
    var orderRows: Set<OrderRow> = HashSet();
}




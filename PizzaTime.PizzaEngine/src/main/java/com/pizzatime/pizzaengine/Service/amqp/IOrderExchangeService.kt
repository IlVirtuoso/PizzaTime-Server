package com.pizzatime.pizzaengine.Service.amqp

import com.pizzatime.pizzaengine.Component.PizzeriaCostForOrder
import java.util.*


data class SubmissionRow(var pizzeriaId: Long, var totalPrice: Double);

data class SubmissionReport(val orderId: String, val rows: Set<PizzeriaCostForOrder>)

interface IOrderExchangeService {
    /**
     * fai cose e dammele (ricorda di copiare l'orderId)
     */
    fun OnOrderSubmitted(order: OrderRecord): SubmissionReport;
}
package com.pizzatime.pizzaengine.Service.amqp

import java.util.*


data class SubmissionRow(var pizzeriaId: Long, var totalPrice: Double);

data class SubmissionReport(val orderId: String, val rows: Set<SubmissionRow>)

interface IOrderExchangeService {
    /**
     * fai cose e dammele (ricorda di copiare l'orderId)
     */
    fun OnOrderSubmitted(order: OrderRecord): SubmissionRow;
}
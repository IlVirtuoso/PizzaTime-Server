package com.PizzaTime.OrderService.Services

import com.PizzaTime.OrderService.Services.Amqp.SagaNotifyService
import com.PizzaTime.OrderService.Services.Amqp.SubmissionReport
import org.springframework.stereotype.Service

@Service
class OrderSagaService(val sagaNotifyService: SagaNotifyService) : IOrderSagaService {
    override fun startPizzeriaNegotiation(submissionReport: SubmissionReport) {
        TODO("Not yet implemented")
    }

    override fun handleBalanceNotification(orderId: String, failure: Boolean) {
        TODO("Not yet implemented")
    }


}
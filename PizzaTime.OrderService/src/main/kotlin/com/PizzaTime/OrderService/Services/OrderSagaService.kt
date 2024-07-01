package com.PizzaTime.OrderService.Services

import com.PizzaTime.OrderService.Model.OrderStatus
import com.PizzaTime.OrderService.Services.Amqp.SagaNotifyService
import com.PizzaTime.OrderService.Services.Amqp.SubmissionReport
import org.springframework.stereotype.Service

@Service
class OrderSagaService(val sagaNotifyService: SagaNotifyService, val orderService: IOrderService) : IOrderSagaService {
    override fun startPizzeriaNegotiation(submissionReport: SubmissionReport) {
        var row = submissionReport.rows.iterator().next();
        val order = orderService.getOrderById(submissionReport.orderId).get();
        order.pizzeriaId  = row.pizzeriaId;
        order.orderStatus  = OrderStatus.ACCEPTED.status;
        order.totalPrice = row.cost;
        orderService.save(order);
    }

    override fun handleBalanceNotification(orderId: String, failure: Boolean) {
        val order = orderService.getOrderById(orderId).get();
        order.orderStatus = OrderStatus.SERVING.status;
    }


}
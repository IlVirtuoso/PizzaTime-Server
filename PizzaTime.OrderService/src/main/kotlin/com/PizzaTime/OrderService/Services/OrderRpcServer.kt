package com.PizzaTime.OrderService.Services

import com.rabbitmq.client.Channel
import com.rabbitmq.client.StringRpcServer

class OrderRpcServer : StringRpcServer {

    companion object {
        const val rpc_exchange_name = "order/rpc/json";
    }

    constructor(channel: Channel) : super(channel){
        channel.exchangeDeclare(rpc_exchange_name,"direct");
        channel.queueBind(this.queueName,rpc_exchange_name,"jsonRequest");
    }
    override fun handleStringCall(request: String?): String {
        when(request){
            "Move" ->  return "r";
        }

        return "NOT_SUPPORTED";
    }
}
package com.PizzaTime.OrderService.Messages

enum class Type(type: String) {
    RESPONSE("response"),
    ERROR("error"),
}


open class GenericOrderResponse(var type: Type);
open class ErrorResponse(val reason: String) : GenericOrderResponse(Type.ERROR);
open class ResponseMessage<T>(val payload:T) : GenericOrderResponse(Type.RESPONSE)
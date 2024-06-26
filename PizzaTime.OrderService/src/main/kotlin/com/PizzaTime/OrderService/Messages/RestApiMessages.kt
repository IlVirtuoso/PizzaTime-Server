package com.PizzaTime.OrderService.Messages

import com.PizzaTime.OrderService.Model.asJson


enum class Type(type: String) {
    RESPONSE("response"),
    ERROR("error"),
}


open class GenericOrderResponse(var type: Type, var reason: String, var payload: String);

open class ResultResponse<T>(var load: T):
        GenericOrderResponse(
            Type.RESPONSE,
            "",
            load.asJson()
        );


open class ErrorResponse(
    var message: String
): GenericOrderResponse(Type.ERROR,message,"");
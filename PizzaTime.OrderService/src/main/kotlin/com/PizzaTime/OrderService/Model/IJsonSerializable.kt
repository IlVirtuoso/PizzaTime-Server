package com.PizzaTime.OrderService.Model

import com.google.gson.GsonBuilder

interface IJsonSerializable {
    fun toJson(): String{
        return GsonBuilder().excludeFieldsWithoutExposeAnnotation().create().toJson(this);
    }
}
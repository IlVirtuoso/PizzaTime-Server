package com.pizzatime.pizzaengine.Service.amqp

import com.google.gson.Gson
import com.google.gson.GsonBuilder


 fun <T> T.asJson(useAttrs : Boolean = false): String {
    if (useAttrs) {
        return GsonBuilder().excludeFieldsWithoutExposeAnnotation().create().toJson(this);
    }
    return Gson().toJson(this)
}

 inline fun <reified T> fromJson(jsonString: String): T {
    return Gson().fromJson(jsonString, T::class.java)
}


package com.pizzatime.pizzatimeservice.controllers

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController


@RestController
class TestController{


    @GetMapping(path = ["/test"])
    fun hello(): String {
        return "hello"
    }


}

package com.pizzatime.pizzatimeservice

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.web.bind.annotation.GetMapping

@SpringBootApplication
class PizzatimeserviceApplication

fun main(args: Array<String>) {
    runApplication<PizzatimeserviceApplication>(*args)
}


@GetMapping("/")
fun index(): String{
    return "forward:/index.html"
}


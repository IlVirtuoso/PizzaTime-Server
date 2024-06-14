package com.PizzaTime.OrderService.Services

import com.PizzaTime.OrderService.Order
import kotlinx.coroutines.Deferred
import java.util.concurrent.Future

interface ICommunicationService {
    suspend fun getUserFromToken(token : String) : Deferred<String>;
    suspend fun sumPizzasPrice(pizzas : List<Long>) : Deferred<Double> ;
    suspend fun isUserAdminForPizzeria(userToken: String, piva: String) : Deferred<Boolean>;
    suspend fun searchAvailablePizzeriaForOrder(userId: String, pizzas: List<Long>) : Deferred<String>;
}
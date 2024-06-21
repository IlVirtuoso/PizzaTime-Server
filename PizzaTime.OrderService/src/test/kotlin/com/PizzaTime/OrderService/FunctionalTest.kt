package com.PizzaTime.OrderService

import kotlinx.coroutines.GlobalScope
import org.junit.jupiter.api.Test
import kotlin.coroutines.createCoroutine
import kotlin.coroutines.startCoroutine
import kotlinx.coroutines.async;
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.runBlocking

class FunctionalTest {

    @Test
    fun testVoidLoad(){

    }

    @Test
    fun testLockableAwait(){

        val lock = runBlocking {
            getSomething();
        }
        println(lock);
    }

    suspend fun getSomething(): Int{
        suspend {
            Thread.sleep(1000);
        }.invoke();
        return 42;
    }
}
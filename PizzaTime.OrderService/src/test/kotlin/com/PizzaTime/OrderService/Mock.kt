package com.PizzaTime.OrderService

import kotlin.reflect.full.declaredFunctions
import kotlin.reflect.jvm.isAccessible

class Mock {
    private val events = mutableMapOf<String, (Array<Any?>) -> Any?>()

    init {
        // Usa la riflessione per ottenere tutti i metodi dichiarati nella classe del target
        val kClass = this::class
        kClass.declaredFunctions.forEach { kFunction ->
            // Rendi il metodo accessibile se non lo è
            kFunction.isAccessible = true

            // Crea una lambda che può essere chiamata con un array di parametri
            val eventLambda: (Array<Any?>) -> Any? = { args ->
                // Prepara gli argomenti per la chiamata al metodo
                val params = listOf(kClass) + args.toList()
                kFunction.call(*params.toTypedArray())

            }

            // Aggiungi la lambda alla mappa degli eventi con il nome del metodo
            events[kFunction.name] = eventLambda
        }
    }
}
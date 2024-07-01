package com.pizzatime.pizzaengine.Component;

public class PizzeriaCostForOrder {
    public Long pizzeriaId;
    public Float cost;

    public PizzeriaCostForOrder(Long pid, Float c){
        this.pizzeriaId=pid;
        this.cost=c;
    }
}
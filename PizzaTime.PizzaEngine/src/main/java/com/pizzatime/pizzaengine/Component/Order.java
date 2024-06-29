package com.pizzatime.pizzaengine.Component;

import java.util.ArrayList;

public class Order{
    public Long pizzeriaId;
    public ArrayList<OrderRows> order;

    public Order(Long pid, ArrayList<OrderRows> o){
        this.pizzeriaId=pid;
        this.order=o;
    }



}
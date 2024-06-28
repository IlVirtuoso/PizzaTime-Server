package com.pizzatime.pizzaengine.Service;

import com.pizzatime.pizzaengine.Component.Order;
import com.pizzatime.pizzaengine.Component.PizzeriaCostForOrder;
import com.pizzatime.pizzaengine.Controller.SearchEngineController;
import com.pizzatime.pizzaengine.Service.amqp.*;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class OrderExchangeService implements IOrderExchangeService {

    @Autowired
    MenuService menuService;

    @NotNull
    @Override
    public SubmissionReport OnOrderSubmitted(@NotNull OrderRecord order) {

        // Convert the OrderRecord into an internal Order
        Order o = menuService.convertOrderAMQPtoInternal(order);

        //Search all pizzerias that can handle the order
        List<PizzeriaCostForOrder> list = menuService.searchPizzeriaForOrderWithCost(o);

        if(list == null && list.isEmpty()){
            list = new ArrayList<PizzeriaCostForOrder>();
        }
        SubmissionReport report = new SubmissionReport(order.getId(), new HashSet<PizzeriaCostForOrder>(list));

        return report;
    }
}

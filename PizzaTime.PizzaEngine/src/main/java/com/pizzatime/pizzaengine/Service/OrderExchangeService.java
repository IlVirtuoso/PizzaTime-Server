package com.pizzatime.pizzaengine.Service;

import com.pizzatime.pizzaengine.Service.amqp.IOrderExchangeService;
import com.pizzatime.pizzaengine.Service.amqp.OrderRecord;
import com.pizzatime.pizzaengine.Service.amqp.SubmissionRow;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

@Service
public class OrderExchangeService implements IOrderExchangeService {

    @NotNull
    @Override
    public SubmissionRow OnOrderSubmitted(@NotNull OrderRecord order) {
        return null;
    }
}

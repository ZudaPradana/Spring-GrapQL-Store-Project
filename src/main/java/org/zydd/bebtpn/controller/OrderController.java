package org.zydd.bebtpn.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import org.zydd.bebtpn.dto.*;
import org.zydd.bebtpn.service.OrderService;

@Controller
public class OrderController {
    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @QueryMapping
    public ResponGetAllData<?> getAllOrder(@Argument String customerName,@Argument String itemName,@Argument int page,@Argument int size) {
        return orderService.getAllOrder(customerName, itemName, page, size);
    }

    @QueryMapping
    public ResponGetData<?> getOrder(@Argument String orderId) {
        return orderService.getOrder(orderId);
    }

    @MutationMapping
    public ResponHeader createOrder(@Argument("createOrder") RequestOrderCreate request) {
        return orderService.createOrder(request);
    }

    @MutationMapping
    public ResponHeader updateOrder(@Argument String id,@Argument("updateOrder") RequestOrderUpdate request) {
        return orderService.updateOrder(id,request);
    }

    @MutationMapping
    public ResponHeader deleteOrder(@Argument String id) {
        return orderService.deleteOrder(id);
    }
}

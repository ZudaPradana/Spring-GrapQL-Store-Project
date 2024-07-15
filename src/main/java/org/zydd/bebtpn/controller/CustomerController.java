package org.zydd.bebtpn.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import org.zydd.bebtpn.dto.*;
import org.zydd.bebtpn.service.CustomerService;

@Controller
public class CustomerController {
    private final CustomerService service;

    @Autowired
    public CustomerController(CustomerService service) {
        this.service = service;
    }


    @QueryMapping
    public ResponGetAllData<?> getAllCustomer(@Argument String name, @Argument Boolean isActive, @Argument int page, @Argument int size) {
        return service.getAllCustomers(name, isActive, page, size);
    }


    @QueryMapping
    public ResponGetData getCustomerById(@Argument String customerId) {
        return service.getCustomerById(customerId);
    }

    @MutationMapping
    public ResponHeader updateCustomer(@Argument String id, @Argument("updateCustomer") RequestCustomerUpdate request) {
        return service.updateCustomer(id, request);
    }

    @MutationMapping
    public ResponHeader createCustomer(@Argument("createCustomer") RequestCustomerCreate request) {
        return service.createCustomer(request);
    }

    @MutationMapping
    public ResponHeader deleteCustomer(@Argument String id) {
        return service.deleteCustomer(id);
    }
}

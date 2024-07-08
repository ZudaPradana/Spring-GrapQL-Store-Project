package org.zydd.bebtpn.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import org.zydd.bebtpn.dto.RequestCustomerCreate;
import org.zydd.bebtpn.dto.RequestCustomerUpdate;
import org.zydd.bebtpn.entity.Customers;
import org.zydd.bebtpn.service.CustomerService;

import java.util.List;

@Controller
public class CustomerController {
    private final CustomerService service;

    @Autowired
    public CustomerController(CustomerService service) {
        this.service = service;
    }


    @QueryMapping
    public List<Customers> getAllCustomer(@Argument String name, @Argument Boolean isActive, @Argument int page, @Argument int size) {
        Page<Customers> response = service.getAllCustomers(name, isActive, page, size);
        return response.getContent();
    }

    @QueryMapping
    public Customers getCustomerById(@Argument String customerId) {
        return service.getCustomerById(customerId);
    }

    @MutationMapping
    public Customers updateCustomer(@Argument String id, @Argument("updateCustomer") RequestCustomerUpdate request) {
        return service.updateCustomer(id, request);
    }

    @MutationMapping
    public Customers createCustomer(@Argument("createCustomer") RequestCustomerCreate request) {
        return service.createCustomer(request);
    }

    @MutationMapping
    public Boolean deleteCustomer(@Argument String id) {
        service.deleteCustomer(id);
        return true;
    }
}

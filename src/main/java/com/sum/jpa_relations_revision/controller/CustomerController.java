package com.sum.jpa_relations_revision.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.sum.jpa_relations_revision.dto.CustomerRequest;
import com.sum.jpa_relations_revision.dto.CustomerResponse;
import com.sum.jpa_relations_revision.service.CustomerService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService service;

    @PostMapping("/customers")
    public String addCustomer(@RequestBody CustomerRequest request) {
        return service.insertCustomer(request);
    }

    @GetMapping("/customers")
    public List<CustomerResponse> fetchCustomers() {
        return service.fetchAllCustomers();
    }
}

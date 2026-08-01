package com.sum.jpa_relations_revision.service;

import com.sum.jpa_relations_revision.repository.OrderRepository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.sum.jpa_relations_revision.dto.CustomerRequest;
import com.sum.jpa_relations_revision.dto.CustomerResponse;
import com.sum.jpa_relations_revision.dto.OrderRequest;
import com.sum.jpa_relations_revision.dto.OrderResponse;
import com.sum.jpa_relations_revision.entity.Customer;
import com.sum.jpa_relations_revision.entity.Order;
import com.sum.jpa_relations_revision.repository.CustomerRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;

    public String insertCustomer(CustomerRequest request) {

        Customer customer = new Customer();
        customer.setName(request.name());
        
        List<Order> orders = request.orders().stream()
                .map(orderRequest -> {
                    Order order = new Order();
                    order.setProduct(orderRequest.product());
                    order.setCustomer(customer);

                    return order;
                })
                .toList();

        customer.setOrders(orders);

        customerRepository.save(customer);

        return "Customer is created";
    }

    public List<CustomerResponse> fetchAllCustomers() {
        List<Customer> customers = customerRepository.findAll();

        List<CustomerResponse> customerResponse = customers.stream()
                .map(customer -> {
                    CustomerResponse response = new CustomerResponse(
                        customer.getId(),
                        customer.getName(),
                        customer.getOrders().stream()
                                .map(order -> {
                                    OrderResponse orderResponse = new OrderResponse(
                                        order.getId(),
                                        order.getProduct()
                                    );

                                    return orderResponse;
                                })
                                .toList()
                    );

                    return response;
                })
                .toList();

        return customerResponse;
    }
}

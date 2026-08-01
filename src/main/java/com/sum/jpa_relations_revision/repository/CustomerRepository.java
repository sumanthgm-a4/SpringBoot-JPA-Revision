package com.sum.jpa_relations_revision.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sum.jpa_relations_revision.entity.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Integer> {

}

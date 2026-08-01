package com.sum.jpa_relations_revision.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sum.jpa_relations_revision.entity.Order;

public interface OrderRepository extends JpaRepository<Order, Integer> {

}

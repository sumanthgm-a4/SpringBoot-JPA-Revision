package com.sum.jpa_relations_revision.dto;

import java.util.List;

public record CustomerResponse(
    Integer customerId,
    String customerName,
    List<OrderResponse> orders
) {}

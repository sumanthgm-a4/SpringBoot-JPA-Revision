package com.sum.jpa_relations_revision.dto;

import java.util.List;

public record CustomerRequest(
    String name,
    List<OrderRequest> orders
) {}

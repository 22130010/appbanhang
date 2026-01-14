package com.example.appbanhang.order;

import com.example.appbanhang.order.dto.CreateOrderRequest;
import com.example.appbanhang.order.dto.OrderResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin
public class OrderController {
    private final OrderService service;

    public OrderController(OrderService service) {
        this.service = service;
    }

    @GetMapping
    public List<OrderResponse> list() {
        return service.listAll();
    }

    @GetMapping("/{id}")
    public OrderResponse get(@PathVariable Long id) {
        return service.get(id);
    }

    @PostMapping
    public OrderResponse create(@Valid @RequestBody CreateOrderRequest req) {
        return service.createOrder(req);
    }
}

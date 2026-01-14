package com.example.appbanhang.customer;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/customers")
@CrossOrigin
public class CustomerController {
    private final CustomerRepository repo;

    public CustomerController(CustomerRepository repo) { this.repo = repo; }

    @GetMapping
    public List<Customer> list() { return repo.findAll(); }

    @GetMapping("/{id}")
    public Customer get(@PathVariable Integer id) {
        return repo.findById(id).orElseThrow(() -> new RuntimeException("Customer not found"));
    }

    @PostMapping
    public Customer create(@Valid @RequestBody Customer c) { return repo.save(c); }
}

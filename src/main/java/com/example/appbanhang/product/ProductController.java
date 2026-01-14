package com.example.appbanhang.product;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/products")
@CrossOrigin
public class ProductController {
    private final ProductService service;

    public ProductController(ProductService service) { this.service = service; }

    @GetMapping
    public List<Product> list() { return service.listAll(); }

    @GetMapping("/{id}")
    public Product get(@PathVariable Integer id) { return service.get(id); }

    @PostMapping
    public Product create(@Valid @RequestBody Product p) { return service.create(p); }

    @PutMapping("/{id}")
    public Product update(@PathVariable Integer id, @Valid @RequestBody Product p) { return service.update(id, p); }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) { service.delete(id); }
}

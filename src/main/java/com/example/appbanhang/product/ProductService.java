package com.example.appbanhang.product;

import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ProductService {
    private final ProductRepository repo;

    public ProductService(ProductRepository repo) { this.repo = repo; }

    public List<Product> listAll() { return repo.findAll(); }
    public Product get(Integer id) { return repo.findById(id).orElseThrow(() -> new RuntimeException("Product not found")); }
    public Product create(Product p) { return repo.save(p); }
    public Product update(Integer id, Product p) {
        Product old = get(id);
        old.setName(p.getName());
        old.setPrice(p.getPrice());
        old.setDescription(p.getDescription());
        old.setImageUrl(p.getImageUrl());
        return repo.save(old);
    }
    public void delete(Integer id) { repo.deleteById(id); }
}

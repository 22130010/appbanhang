package com.example.appbanhang.order;

import com.example.appbanhang.customer.Customer;
import com.example.appbanhang.customer.CustomerRepository;
import com.example.appbanhang.order.dto.CreateOrderRequest;
import com.example.appbanhang.order.dto.OrderResponse;
import com.example.appbanhang.product.Product;
import com.example.appbanhang.product.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {
    private final OrderRepository orderRepo;
    private final ProductRepository productRepo;
    private final CustomerRepository customerRepo;

    public OrderService(OrderRepository orderRepo, ProductRepository productRepo, CustomerRepository customerRepo) {
        this.orderRepo = orderRepo;
        this.productRepo = productRepo;
        this.customerRepo = customerRepo;
    }

    @Transactional
    public OrderResponse createOrder(CreateOrderRequest req) {
        Customer customer = customerRepo.findByPhone(req.getPhone())
                .orElseGet(() -> {
                    Customer c = new Customer();
                    c.setFullName(req.getFullName());
                    c.setPhone(req.getPhone());
                    c.setAddress(req.getAddress());
                    return customerRepo.save(c);
                });

        Order order = new Order();
        order.setCustomer(customer);
        order.setPaymentMethod("transfer".equalsIgnoreCase(req.getPaymentMethod())
                ? PaymentMethod.TRANSFER
                : PaymentMethod.CASH);

        int total = 0;
        for (CreateOrderRequest.Item it : req.getItems()) {
            Product p = productRepo.findById(it.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found: " + it.getProductId()));
            OrderItem oi = new OrderItem();
            oi.setOrder(order);
            oi.setProduct(p);
            oi.setQuantity(it.getQuantity());
            oi.setPrice(p.getPrice());
            order.getItems().add(oi);
            total += p.getPrice() * it.getQuantity();
        }
        order.setTotalAmount(total);

        Order saved = orderRepo.save(order);
        return toResponse(saved);
    }

    public OrderResponse get(Long id) {
        return toResponse(orderRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found")));
    }

    public List<OrderResponse> listAll() {
        return orderRepo.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    private OrderResponse toResponse(Order o) {
        List<OrderResponse.Line> lines = o.getItems().stream()
                .map(oi -> {
                    OrderResponse.Line line = new OrderResponse.Line();
                    line.setProductId(oi.getProduct().getId());
                    line.setProductName(oi.getProduct().getName());
                    line.setPrice(oi.getPrice());
                    line.setQuantity(oi.getQuantity());
                    return line;
                }).collect(Collectors.toList());

        OrderResponse res = new OrderResponse();
        res.setId(o.getId());
        res.setFullName(o.getCustomer().getFullName());
        res.setPhone(o.getCustomer().getPhone());
        res.setAddress(o.getCustomer().getAddress());
        res.setPaymentMethod(o.getPaymentMethod().name().toLowerCase());
        res.setTotalAmount(o.getTotalAmount());
        res.setOrderDate(o.getOrderDate());
        res.setItems(lines);
        return res;
    }
}

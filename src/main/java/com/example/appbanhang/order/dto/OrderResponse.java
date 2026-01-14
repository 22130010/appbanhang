package com.example.appbanhang.order.dto;

import java.time.LocalDateTime;
import java.util.List;

public class OrderResponse {
    private Long id;
    private String fullName;
    private String phone;
    private String address;
    private String paymentMethod;
    private Integer totalAmount;
    private LocalDateTime orderDate;
    private List<Line> items;

    public static class Line {
        private Integer productId;
        private String productName;
        private Integer price;
        private Integer quantity;

        // Getters & Setters
        public Integer getProductId() { return productId; }
        public void setProductId(Integer productId) { this.productId = productId; }

        public String getProductName() { return productName; }
        public void setProductName(String productName) { this.productName = productName; }

        public Integer getPrice() { return price; }
        public void setPrice(Integer price) { this.price = price; }

        public Integer getQuantity() { return quantity; }
        public void setQuantity(Integer quantity) { this.quantity = quantity; }
    }

    // Getters & Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }

    public Integer getTotalAmount() { return totalAmount; }
    public void setTotalAmount(Integer totalAmount) { this.totalAmount = totalAmount; }

    public LocalDateTime getOrderDate() { return orderDate; }
    public void setOrderDate(LocalDateTime orderDate) { this.orderDate = orderDate; }

    public List<Line> getItems() { return items; }
    public void setItems(List<Line> items) { this.items = items; }
}

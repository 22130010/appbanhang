package com.example.appbanhang.data.dto

data class OrderResponse(
    val id: Long,
    val fullName: String,
    val phone: String,
    val address: String,
    val paymentMethod: String,
    val totalAmount: Int,
    val orderDate: String,
    val items: List<Line>
) {
    data class Line(
        val productId: Int,
        val productName: String,
        val price: Int,
        val quantity: Int
    )
}

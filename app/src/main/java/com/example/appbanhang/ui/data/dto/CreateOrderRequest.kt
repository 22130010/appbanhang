package com.example.appbanhang.data.dto

data class CreateOrderRequest(
    val fullName: String,
    val phone: String,
    val address: String,
    val paymentMethod: String,
    val items: List<Item>
) {
    data class Item(
        val productId: Int,
        val quantity: Int
    )
}

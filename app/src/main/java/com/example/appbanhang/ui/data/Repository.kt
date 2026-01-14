package com.example.appbanhang.data

import com.example.appbanhang.data.dto.CreateOrderRequest
import com.example.appbanhang.data.dto.OrderResponse
import com.example.appbanhang.network.ApiClient


object Repository {
    suspend fun fetchProducts(): List<MonAn> {
        val dtos = ApiClient.api.getProducts()
        return dtos.map {
            MonAn(
                id = it.id.toString(),
                ten = it.name,
                gia = it.price,
                moTa = it.description,
                hinhAnh = it.imageUrl
            )
        }
    }

    suspend fun createOrder(
        items: List<CartItem>,
        hoTen: String,
        sdt: String,
        diaChi: String,
        thanhToan: String
    ): OrderResponse {
        val request = CreateOrderRequest(
            fullName = hoTen,
            phone = sdt,
            address = diaChi,
            paymentMethod = if (thanhToan.equals("Chuyển khoản", true)) "transfer" else "cash",
            items = items.map {
                CreateOrderRequest.Item(
                    productId = it.monAn.id.toInt(),
                    quantity = it.soLuong
                )
            }
        )
        return ApiClient.api.createOrder(request)
    }

    suspend fun fetchOrders(): List<OrderResponse> {
        return ApiClient.api.getOrders()
    }
}

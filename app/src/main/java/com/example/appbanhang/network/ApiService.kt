package com.example.appbanhang.network

import com.example.appbanhang.data.dto.CreateOrderRequest
import com.example.appbanhang.data.dto.OrderResponse
import com.example.appbanhang.data.dto.ProductDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

/**
 * Định nghĩa các endpoint cho API.
 * QUAN TRỌNG: Các đường dẫn này phải khớp chính xác với backend của bạn.
 */
interface ApiService {
    // Yêu cầu GET đến http://<base_url>/products
    @GET("products")
    suspend fun getProducts(): List<ProductDto>

    // Yêu cầu POST đến http://<base_url>/orders
    @POST("orders")
    suspend fun createOrder(@Body request: CreateOrderRequest): OrderResponse

    // Yêu cầu GET đến http://<base_url>/orders
    @GET("orders")
    suspend fun getOrders(): List<OrderResponse>
}

package com.example.appbanhang.network

import com.example.appbanhang.data.dto.CreateOrderRequest
import com.example.appbanhang.data.dto.OrderResponse
import com.example.appbanhang.data.dto.ProductDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {
    @GET("api/products")
    suspend fun getProducts(): List<ProductDto>

    @POST("api/orders")
    suspend fun createOrder(@Body request: CreateOrderRequest): OrderResponse

    @GET("api/orders")
    suspend fun getOrders(): List<OrderResponse>


}

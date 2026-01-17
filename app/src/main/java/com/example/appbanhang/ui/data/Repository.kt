package com.example.appbanhang.data

import com.example.appbanhang.ui.viewModel.Customer
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// --- LỚP DỮ LIỆU ĐÃ CẬP NHẬT ---
data class FirebaseOrderItem(
    val productId: String = "",
    val productName: String = "",
    val price: Int = 0,
    val quantity: Int = 0
) { constructor() : this("", "", 0, 0) }

data class FirebaseOrder(
    var id: String = "",
    val customerId: String = "",
    val customerName: String = "",
    val address: String = "",
    val phone: String = "",
    // THAY ĐỔI QUAN TRỌNG: Quay trở lại sử dụng List để nhất quán
    val items: List<FirebaseOrderItem> = emptyList(),
    val totalAmount: Double = 0.0,
    val paymentMethod: String = "",
    val orderDate: String = "",
    val status: String = "PENDING"
) { constructor() : this("", "", "", "", "", emptyList(), 0.0, "", "", "PENDING") }


object Repository {

    private val productRef = FirebaseDatabase.getInstance().getReference("products")
    private val orderRef = FirebaseDatabase.getInstance().getReference("orders")

    suspend fun fetchProducts(): List<MonAn> {
        val snapshot = productRef.get().await()
        return snapshot.children.mapNotNull { dataSnapshot ->
            dataSnapshot.getValue(MonAn::class.java)?.apply {
                id = dataSnapshot.key ?: ""
            }
        }
    }

    suspend fun createOrder(customer: Customer, cartItems: List<CartItem>, paymentMethod: String): String {
        val orderId = orderRef.push().key ?: throw Exception("Không thể tạo mã đơn hàng")
        val currentDate = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault()).format(Date())
        val total = cartItems.sumOf { it.monAn.price * it.soLuong.toDouble() }

        val newOrder = FirebaseOrder(
            id = orderId,
            customerId = customer.id,
            customerName = customer.full_name,
            address = customer.address,
            phone = customer.phone,
            // SỬA LỖI: Chuyển đổi List<CartItem> thành List<FirebaseOrderItem>
            items = cartItems.map { cartItem ->
                FirebaseOrderItem(
                    productId = cartItem.monAn.id,
                    productName = cartItem.monAn.name,
                    price = cartItem.monAn.price,
                    quantity = cartItem.soLuong
                )
            },
            totalAmount = total,
            paymentMethod = paymentMethod,
            orderDate = currentDate,
            status = "PENDING"
        )

        orderRef.child(orderId).setValue(newOrder).await()
        return orderId
    }

    // THAY ĐỔI: Hàm này giờ nhận vào customerId để lọc đơn hàng
    suspend fun fetchOrders(customerId: String): List<FirebaseOrder> {
        val snapshot = orderRef.orderByChild("customerId").equalTo(customerId).get().await()
        return snapshot.children.mapNotNull { it.getValue(FirebaseOrder::class.java) }.sortedByDescending { it.orderDate }
    }
}

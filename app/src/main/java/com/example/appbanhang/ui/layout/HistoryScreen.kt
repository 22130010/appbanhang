package com.example.appbanhang.ui.layout

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.appbanhang.ui.viewModel.HistoryState
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun formatFirebaseDate(dateString: String): String {
    return try {
        val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
        val formatter = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        formatter.format(parser.parse(dateString) as Date)
    } catch (e: Exception) {
        dateString
    }
}

@Composable
fun HistoryScreen(
    state: HistoryState,
    customerId: String, // Nhận vào customerId
    onReload: (String) -> Unit // onReload giờ cũng nhận customerId
) {
    // Tự động tải lại lịch sử khi màn hình được hiển thị lần đầu hoặc khi customerId thay đổi
    LaunchedEffect(customerId) {
        onReload(customerId)
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Lịch sử đặt hàng", style = MaterialTheme.typography.headlineMedium)
            Spacer(Modifier.weight(1f))
            IconButton(onClick = { onReload(customerId) }) {
                // Thêm icon refresh nếu muốn
            }
        }
        Spacer(Modifier.height(16.dp))

        if (state.isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else if (state.error != null) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Lỗi: ${state.error}", color = MaterialTheme.colorScheme.error)
            }
        } else if (state.orders.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Bạn chưa có đơn hàng nào.")
            }
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                items(state.orders) { order ->
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("Mã đơn: ${order.id}", style = MaterialTheme.typography.titleMedium)
                            Spacer(Modifier.height(4.dp))
                            Text("Ngày đặt: ${formatFirebaseDate(order.orderDate)}")
                            Text("Tổng tiền: ${order.totalAmount} VND")
                            Text("Trạng thái: ${order.status}", fontWeight = FontWeight.Bold)
                            Spacer(Modifier.height(8.dp))
                            Text("Sản phẩm:", style = MaterialTheme.typography.titleSmall)
                            order.items.forEach {
                                Text("- ${it.productName} (SL: ${it.quantity})")
                            }
                        }
                    }
                }
            }
        }
    }
}

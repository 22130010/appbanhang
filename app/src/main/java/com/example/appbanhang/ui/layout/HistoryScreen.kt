package com.example.appbanhang.ui.layout

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.appbanhang.data.dto.OrderResponse
import com.example.appbanhang.ui.viewModel.HistoryState
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun HistoryScreen(
    state: HistoryState,
    onReload: () -> Unit
) {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Lịch sử đơn hàng", style = MaterialTheme.typography.titleLarge)
        Spacer(Modifier.height(8.dp))

        if (state.dangTai) {
            LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
        }
        state.loi?.let { Text("Lỗi: $it", color = MaterialTheme.colorScheme.error) }

        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(state.orders) { order ->
                Card {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text("Mã đơn: #${order.id}", style = MaterialTheme.typography.titleMedium)
                        Text("Khách: ${order.fullName}")
                        Text("Tổng tiền: ${order.totalAmount} VND")
                        Text("Thanh toán: ${order.paymentMethod}")
                        Spacer(Modifier.height(6.dp))
                        order.items.forEach {
                            Text("- ${it.productName} x${it.quantity} (${it.price} VND)")
                        }
                    }
                }
            }
        }

        Spacer(Modifier.height(8.dp))
        OutlinedButton(onClick = onReload, modifier = Modifier.fillMaxWidth()) {
            Text("Tải lại")
        }
    }
}
@Preview(showBackground = true, name = "Lịch sử đơn hàng - Preview")
@Composable
fun PreviewHistoryScreen() {
    val sampleOrders = listOf(
        OrderResponse(
            id = 1,
            fullName = "Nguyen Van A",
            phone = "0987654321",
            address = "HCM",
            paymentMethod = "cash",
            totalAmount = 320000,
            orderDate = "2026-01-14",
            items = listOf(
                OrderResponse.Line(1, "Bò sốt tiêu đen", 160000, 1),
                OrderResponse.Line(2, "Gà rang muối", 120000, 2)
            )
        )
    )
    val sampleState = HistoryState(orders = sampleOrders)

    HistoryScreen(
        state = sampleState,
        onReload = {}
    )
}


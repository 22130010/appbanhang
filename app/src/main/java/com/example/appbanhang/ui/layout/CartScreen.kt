package com.example.appbanhang.ui.layout

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.appbanhang.data.CartItem
import com.example.appbanhang.data.MonAn
import com.example.appbanhang.ui.viewModel.Customer

import com.example.appbanhang.ui.viewModel.GioHangState

@Composable
fun CartScreen(
    state: GioHangState,
    currentUser: Customer?, // Nhận vào người dùng hiện tại
    onUpdateQuantity: (CartItem, Int) -> Unit,
    onRemoveItem: (CartItem) -> Unit,
    onPlaceOrder: (Customer, String) -> Unit // Truyền customer và payment method
) {
    var showConfirmDialog by remember { mutableStateOf(false) }
    var paymentMethod by remember { mutableStateOf("Tiền mặt") }

    val tongTien = state.cartItems.sumOf { (it.monAn.price * it.soLuong).toDouble() }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Giỏ hàng", style = MaterialTheme.typography.titleLarge)
        Spacer(Modifier.height(12.dp))

        LazyColumn(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(state.cartItems) { item ->
                Card(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        item.monAn.image_url?.let {
                            AsyncImage(
                                model = it, contentDescription = item.monAn.name, modifier = Modifier.size(64.dp)
                            )
                            Spacer(Modifier.width(12.dp))
                        }
                        Column(modifier = Modifier.weight(1f)) {
                            Text(item.monAn.name, style = MaterialTheme.typography.titleMedium)
                            Text("${item.monAn.price} VND")
                        }
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            IconButton(onClick = { onUpdateQuantity(item, item.soLuong - 1) }) { Text("-") }
                            Text("${item.soLuong}", modifier = Modifier.padding(horizontal = 8.dp))
                            IconButton(onClick = { onUpdateQuantity(item, item.soLuong + 1) }) { Text("+") }
                        }
                        TextButton(onClick = { onRemoveItem(item) }) { Text("Xóa") }
                    }
                }
            }
        }

        Spacer(Modifier.height(16.dp))
        Text("Tổng tiền: $tongTien VND", style = MaterialTheme.typography.titleMedium)

        if (state.dangDat) {
            Spacer(Modifier.height(8.dp))
            LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
        }
        state.ketQua?.let { Text(it, color = MaterialTheme.colorScheme.primary) }
        state.loi?.let { Text(it, color = MaterialTheme.colorScheme.error) }

        Spacer(Modifier.height(12.dp))
        Button(
            onClick = { showConfirmDialog = true },
            modifier = Modifier.fillMaxWidth(),
            enabled = state.cartItems.isNotEmpty() && !state.dangDat
        ) { Text("Tiến hành đặt hàng") }

        if (showConfirmDialog) {
            AlertDialog(
                onDismissRequest = { showConfirmDialog = false },
                title = { Text("Xác nhận đặt hàng") },
                text = {
                    Column {
                        Text("Vui lòng chọn phương thức thanh toán.")
                        Spacer(Modifier.height(8.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            FilterChip(selected = paymentMethod == "Tiền mặt", onClick = { paymentMethod = "Tiền mặt" }, label = { Text("Tiền mặt") })
                            FilterChip(selected = paymentMethod == "Chuyển khoản", onClick = { paymentMethod = "Chuyển khoản" }, label = { Text("Chuyển khoản") })
                        }
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            currentUser?.let { user ->
                                onPlaceOrder(user, paymentMethod)
                            }
                            showConfirmDialog = false
                        }
                    ) { Text("Xác nhận") }
                },
                dismissButton = {
                    OutlinedButton(onClick = { showConfirmDialog = false }) { Text("Hủy") }
                }
            )
        }
    }
}

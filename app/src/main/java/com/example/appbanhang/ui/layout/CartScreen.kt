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

import com.example.appbanhang.ui.viewModel.GioHangState

@Composable
fun CartScreen(
    state: GioHangState,
    onUpdateQuantity: (CartItem, Int) -> Unit,
    onRemoveItem: (CartItem) -> Unit,
    onPlaceOrder: (String, String, String, String) -> Unit
) {
    var showOrderForm by remember { mutableStateOf(false) }
    var hoTen by remember { mutableStateOf("") }
    var sdt by remember { mutableStateOf("") }
    var diaChi by remember { mutableStateOf("") }
    var thanhToan by remember { mutableStateOf("Tiền mặt") }

    val tongTien = state.cartItems.sumOf { it.monAn.gia * it.soLuong }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Giỏ hàng", style = MaterialTheme.typography.titleLarge)
        Spacer(Modifier.height(12.dp))

        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(state.cartItems) { item ->
                Card(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Hiển thị ảnh sản phẩm nếu có
                        item.monAn.hinhAnh?.let { url ->
                            AsyncImage(
                                model = url,
                                contentDescription = item.monAn.ten,
                                modifier = Modifier.size(64.dp)
                            )
                            Spacer(Modifier.width(12.dp))
                        }

                        Column(modifier = Modifier.weight(1f)) {
                            Text(item.monAn.ten, style = MaterialTheme.typography.titleMedium)
                            Text("${item.monAn.gia} VND")
                        }
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            IconButton(onClick = {
                                if (item.soLuong > 1) onUpdateQuantity(item, item.soLuong - 1)
                            }) { Text("-") }
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

        Spacer(Modifier.height(12.dp))
        Button(
            onClick = { showOrderForm = true },
            modifier = Modifier.fillMaxWidth(),
            enabled = state.cartItems.isNotEmpty()
        ) { Text("Đặt hàng") }

        if (state.dangDat) {
            Spacer(Modifier.height(8.dp))
            LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
        }
        state.ketQua?.let { Text(it, color = MaterialTheme.colorScheme.primary) }
        state.loi?.let { Text(it, color = MaterialTheme.colorScheme.error) }

        if (showOrderForm) {
            Spacer(Modifier.height(16.dp))
            Text("Thông tin khách hàng", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(8.dp))

            OutlinedTextField(value = hoTen, onValueChange = { hoTen = it }, label = { Text("Họ tên") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = sdt, onValueChange = { sdt = it }, label = { Text("Số điện thoại") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = diaChi, onValueChange = { diaChi = it }, label = { Text("Địa chỉ") }, modifier = Modifier.fillMaxWidth())

            Spacer(Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                FilterChip(selected = thanhToan == "Tiền mặt", onClick = { thanhToan = "Tiền mặt" }, label = { Text("Tiền mặt") })
                FilterChip(selected = thanhToan == "Chuyển khoản", onClick = { thanhToan = "Chuyển khoản" }, label = { Text("Chuyển khoản") })
            }

            Spacer(Modifier.height(12.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedButton(onClick = { showOrderForm = false }) { Text("Hủy") }
                Button(
                    onClick = {
                        onPlaceOrder(hoTen, sdt, diaChi, thanhToan)
                        showOrderForm = false
                    },
                    enabled = hoTen.isNotBlank() && sdt.isNotBlank() && diaChi.isNotBlank()
                ) { Text("Xác nhận đặt hàng") }
            }
        }
    }
}
@Preview(showBackground = true, name = "Giỏ hàng - Preview")
@Composable
fun PreviewCartScreen() {
    val sampleItems = listOf(
        CartItem(monAn = MonAn("1", "Bò sốt tiêu đen", 160000), soLuong = 1),
        CartItem(monAn = MonAn("2", "Gà rang muối", 120000), soLuong = 2)
    )
    val sampleState = GioHangState(cartItems = sampleItems)

    CartScreen(
        state = sampleState,
        onUpdateQuantity = { _, _ -> },
        onRemoveItem = {},
        onPlaceOrder = { _, _, _, _ -> }
    )
}


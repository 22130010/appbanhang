package com.example.appbanhang.ui.layout

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.appbanhang.data.MonAn
import com.example.appbanhang.ui.viewModel.TrangChuState

@Composable
fun HomeScreen(
    state: TrangChuState,
    onSearchChange: (String) -> Unit,
    onReload: () -> Unit,
    onSelectItem: (MonAn) -> Unit
) {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        OutlinedTextField(
            value = state.timKiem,
            onValueChange = onSearchChange,
            placeholder = { Text("Tìm món ăn...") },
            singleLine = true,
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(12.dp))

        if (state.dangTai) LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
        state.loi?.let { Text("Lỗi: $it", color = MaterialTheme.colorScheme.error) }

        val filtered = state.danhSach.filter {
            state.timKiem.isBlank() || it.ten.contains(state.timKiem, ignoreCase = true)
        }

        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            items(filtered, key = { it.id }) { item ->
                ElevatedCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onSelectItem(item) }
                ) {
                    Row(modifier = Modifier.padding(12.dp)) {
                        item.hinhAnh?.let { url ->
                            AsyncImage(
                                model = url,
                                contentDescription = item.ten,
                                modifier = Modifier.size(80.dp)
                            )
                            Spacer(Modifier.width(12.dp))
                        }
                        Column {
                            Text(item.ten, style = MaterialTheme.typography.titleMedium)
                            Text("${item.gia} VND", style = MaterialTheme.typography.bodyMedium)
                            item.moTa?.let { Text(it, style = MaterialTheme.typography.bodySmall) }
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
@Preview(showBackground = true, name = "Trang chủ - Preview")
@Composable
fun PreviewHomeScreen() {
    val sampleState = TrangChuState(
        timKiem = "",
        danhSach = listOf(
            MonAn("1", "Sườn xào chua ngọt", 150000, "Món ăn truyền thống", null),
            MonAn("2", "Bò sốt tiêu đen", 160000, "Thơm ngon đậm vị", null),
            MonAn("3", "Gà rang muối", 120000, "Giòn rụm", null)
        )
    )
    HomeScreen(
        state = sampleState,
        onSearchChange = {},
        onReload = {},
        onSelectItem = {}
    )
}


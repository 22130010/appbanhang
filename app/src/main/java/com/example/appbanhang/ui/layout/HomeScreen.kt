package com.example.appbanhang.ui.layout

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
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
    LaunchedEffect(Unit) {
        onReload() // Tải lại khi vào màn hình
    }

    Column(modifier = Modifier.fillMaxSize().padding(horizontal = 8.dp)) {
        OutlinedTextField(
            value = state.timKiem,
            onValueChange = onSearchChange,
            label = { Text("Tìm kiếm món ăn...") },
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
        )

        if (state.dangTai) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else if (state.loi != null) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Lỗi: ${state.loi}", color = MaterialTheme.colorScheme.error)
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(state.danhSachHienThi) { monAn ->
                    Card(
                        modifier = Modifier.clickable { onSelectItem(monAn) }
                    ) {
                        Column {
                            AsyncImage(
                                model = monAn.image_url,
                                contentDescription = monAn.name,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.height(120.dp).fillMaxWidth()
                            )
                            Column(modifier = Modifier.padding(8.dp)) {
                                Text(monAn.name, style = MaterialTheme.typography.titleMedium, maxLines = 1, overflow = TextOverflow.Ellipsis)
                                Text("${monAn.price} VND", style = MaterialTheme.typography.bodySmall)
                            }
                        }
                    }
                }
            }
        }
    }
}

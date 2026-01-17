package com.example.appbanhang.ui.layout


import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.appbanhang.data.MonAn


@Composable
fun ProductDetailScreen(
    monAn: MonAn,
    onAddToCart: (MonAn) -> Unit
) {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        monAn.image_url?.let { url ->
            AsyncImage(
                model = url,
                contentDescription = monAn.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            )
            Spacer(Modifier.height(12.dp))
        }

        Text(monAn.name, style = MaterialTheme.typography.titleLarge)
        Spacer(Modifier.height(8.dp))
        Text("${monAn.price} VND", style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(8.dp))
        Text(monAn.description ?: "Không có mô tả", style = MaterialTheme.typography.bodyMedium)

        Spacer(Modifier.height(16.dp))
        Button(onClick = { onAddToCart(monAn) }, modifier = Modifier.fillMaxWidth()) {
            Text("Thêm vào giỏ hàng")
        }
    }
}
@Preview(showBackground = true, name = "Chi tiết sản phẩm - Preview")
@Composable
fun PreviewProductDetailScreen() {
    val sampleProduct = MonAn(
        id = "1",
        name = "Bò sốt tiêu đen",
        price = 160000,
        description = "Món ăn đặc biệt với hương vị đậm đà",
        image_url = null
    )
    ProductDetailScreen(
        monAn = sampleProduct,
        onAddToCart = {}
    )
}


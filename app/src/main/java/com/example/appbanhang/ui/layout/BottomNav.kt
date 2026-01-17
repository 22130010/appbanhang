package com.example.appbanhang.ui.layout

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector

data class BottomItem(val route: String, val title: String, val icon: ImageVector)

val bottomItems = listOf(
    BottomItem("home", "Trang chủ", Icons.Filled.Home),
    BottomItem("cart", "Giỏ hàng", Icons.Filled.ShoppingCart),
    BottomItem("history", "Lịch sử", Icons.Filled.List),
)

@Composable
fun BottomBar(currentRoute: String, onNavigate: (String) -> Unit) {
    NavigationBar {
        bottomItems.forEach { item ->
            NavigationBarItem(
                selected = currentRoute == item.route,
                onClick = { onNavigate(item.route) },
                icon = { Icon(item.icon, contentDescription = item.title) },
                label = { Text(item.title) }
            )
        }
    }
}

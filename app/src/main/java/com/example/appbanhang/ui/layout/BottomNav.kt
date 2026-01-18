package com.example.appbanhang.ui.layout

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview

data class BottomItem(val route: String, val title: String, val icon: ImageVector)

val bottomItems = listOf(
    BottomItem("home", "Trang chủ", Icons.Default.Home),
    BottomItem("cart", "Giỏ hàng", Icons.Default.ShoppingCart),
    BottomItem("history", "Lịch sử", Icons.Default.List),
    BottomItem("logout", "Logout", Icons.Default.Logout),
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
@Preview(showBackground = true)
@Composable
fun BottomBarPreview() {
    MaterialTheme {
        BottomBar(
            currentRoute = "home",
            onNavigate = {}
        )
    }
}

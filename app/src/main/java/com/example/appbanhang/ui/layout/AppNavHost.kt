package com.example.appbanhang.ui.layout

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.appbanhang.data.MonAn
import com.example.appbanhang.ui.viewModel.GioHangViewModel
import com.example.appbanhang.ui.viewModel.HistoryViewModel
import com.example.appbanhang.ui.viewModel.TrangChuViewModel

@Composable
fun AppNavHost(
    trangChuVM: TrangChuViewModel,
    gioHangVM: GioHangViewModel,
    historyVM: HistoryViewModel
) {
    val navController = rememberNavController()
    val homeState by trangChuVM.state.collectAsState()
    val cartState by gioHangVM.state.collectAsState()
    val historyState by historyVM.state.collectAsState()

    androidx.compose.material3.Scaffold(
        bottomBar = {
            val currentRoute = navController.currentDestination?.route ?: ""
            BottomBar(currentRoute) { navController.navigate(it) }
        }
    ) { innerPadding ->   // đổi tên biến cho rõ ràng
        NavHost(
            navController = navController,
            startDestination = "home",
            modifier = Modifier.padding(innerPadding) // dùng innerPadding ở đây
        ) {
            composable("home") {
                HomeScreen(
                    state = homeState,
                    onSearchChange = { trangChuVM.capNhatTimKiem(it) },
                    onReload = { trangChuVM.taiDanhSach() },
                    onSelectItem = { monAn ->
                        navController.navigate("detail/${monAn.id}")

                    }
                )
            }
            composable(
                "detail/{id}",
                arguments = listOf(
                    navArgument("id") { type = NavType.StringType }
                )
            ) { backStackEntry ->
                val id = backStackEntry.arguments?.getString("id") ?: ""
                // Lấy chi tiết món ăn từ ViewModel bằng id
                val monAn = trangChuVM.layMonAnTheoId(id)
                monAn?.let { ProductDetailScreen( monAn = it, onAddToCart = { gioHangVM.them(it) } ) }
            }
            composable("cart") {
                CartScreen(
                    state = cartState,
                    onUpdateQuantity = { item, qty -> gioHangVM.capNhatSoLuong(item, qty) },
                    onRemoveItem = { gioHangVM.xoa(it) },
                    onPlaceOrder = { hoTen, sdt, diaChi, thanhToan ->
                        gioHangVM.datHang(hoTen, sdt, diaChi, thanhToan)
                    }
                )
            }
            composable("history") {
                HistoryScreen(
                    state = historyState,
                    onReload = { historyVM.taiHistory() }
                )
            }
        }
    }
}

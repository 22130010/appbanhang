package com.example.appbanhang.ui.layout

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
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

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        bottomBar = {
            if (currentRoute in listOf("home", "cart", "history","logout")) {
                BottomBar(currentRoute ?: "") { route ->
                    if (route == "logout") {
                        navController.navigate("login") {
                            popUpTo(navController.graph.startDestinationId) {
                                inclusive = true
                            }
                            launchSingleTop = true
                        }
                    } else {
                        navController.navigate(route) {
                            launchSingleTop = true
                            restoreState = true
                            popUpTo("home") { saveState = true }
                        }
                    }
                }

            }
        }
    ) { innerPadding ->

        NavHost(
            navController = navController,
            startDestination = "login",
            modifier = Modifier.padding(innerPadding)
        ) {

            composable("login") {
                LoginScreen(
                    onNavigateToRegister = {
                        navController.navigate("register")
                    },
                    onLoginSuccess = {
                        navController.navigate("home") {
                            popUpTo("login") { inclusive = true }
                        }
                    }
                )
            }

            composable("register") {
                RegisterScreen(
                    onNavigateToLogin = {
                        navController.popBackStack()
                    },
                    onRegisterSuccess = {
                        navController.navigate("login") {
                            popUpTo("register") { inclusive = true }
                        }
                    }
                )
            }

            composable("home") {
                HomeScreen(
                    state = homeState,
                    onSearchChange = trangChuVM::capNhatTimKiem,
                    onReload = trangChuVM::taiDanhSach,
                    onSelectItem = { monAn ->
                        navController.navigate("detail/${monAn.id}")
                    }
                )
            }

            composable(
                "detail/{id}",
                arguments = listOf(navArgument("id") { type = NavType.StringType })
            ) { backStackEntry ->
                val id = backStackEntry.arguments?.getString("id") ?: return@composable
                val monAn = trangChuVM.layMonAnTheoId(id) ?: return@composable

                ProductDetailScreen(
                    monAn = monAn,
                    onAddToCart = { gioHangVM.them(it) }
                )
            }

            composable("cart") {
                CartScreen(
                    state = cartState,
                    onUpdateQuantity = gioHangVM::capNhatSoLuong,
                    onRemoveItem = gioHangVM::xoa,
                    onPlaceOrder = gioHangVM::datHang
                )
            }

            composable("history") {
                HistoryScreen(
                    state = historyState,
                    onReload = historyVM::taiHistory
                )
            }
        }
    }
}

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
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.appbanhang.ui.viewModel.AuthViewModel
import com.example.appbanhang.ui.viewModel.AuthState
import com.example.appbanhang.ui.viewModel.GioHangViewModel
import com.example.appbanhang.ui.viewModel.HistoryViewModel
import com.example.appbanhang.ui.viewModel.TrangChuViewModel

@Composable
fun AppNavHost(
    authVM: AuthViewModel,
    trangChuVM: TrangChuViewModel,
    gioHangVM: GioHangViewModel,
    historyVM: HistoryViewModel
) {
    val authState: AuthState by authVM.authState.collectAsState()

    if (authState.isAuthenticated) {
        MainNavigation(authVM, trangChuVM, gioHangVM, historyVM)
    } else {
        AuthNavigation(authVM)
    }
}

@Composable
fun AuthNavigation(authVM: AuthViewModel) {
    val navController = rememberNavController()
    val authState: AuthState by authVM.authState.collectAsState()

    NavHost(navController = navController, startDestination = "login") {
        composable("login") {
            LoginScreen(
                authState = authState,
                onLogin = { username, pass -> authVM.login(username, pass) },
                onNavigateToRegister = { navController.navigate("register") }
            )
        }
        composable("register") {
            RegisterScreen(
                authState = authState,
                onRegister = { username, pass, fullName, phone, address -> 
                    authVM.signUp(username, pass, fullName, phone, address)
                },
                onNavigateToLogin = {
                    navController.navigate("login") {
                        popUpTo("login") { inclusive = true }
                    }
                }
            )
        }
    }
}

@Composable
fun MainNavigation(
    authVM: AuthViewModel,
    trangChuVM: TrangChuViewModel,
    gioHangVM: GioHangViewModel,
    historyVM: HistoryViewModel
) {
    val navController = rememberNavController()
    val authState by authVM.authState.collectAsState()
    val homeState by trangChuVM.state.collectAsState()
    val cartState by gioHangVM.state.collectAsState()
    val historyState by historyVM.state.collectAsState()

    Scaffold(
        bottomBar = {
            val currentRoute = navController.currentBackStackEntry?.destination?.route ?: "home"
            BottomBar(currentRoute) { route ->
                navController.navigate(route) {
                    popUpTo(navController.graph.startDestinationId)
                    launchSingleTop = true
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "home",
            modifier = Modifier.padding(innerPadding)
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
                arguments = listOf(navArgument("id") { type = NavType.StringType })
            ) { backStackEntry ->
                val id = backStackEntry.arguments?.getString("id") ?: ""
                val monAn = trangChuVM.layMonAnTheoId(id)
                monAn?.let { ProductDetailScreen(monAn = it, onAddToCart = { gioHangVM.them(it) }) }
            }
            composable("cart") {
                CartScreen(
                    state = cartState,
                    currentUser = authState.currentUser,
                    onUpdateQuantity = { item, qty -> gioHangVM.capNhatSoLuong(item, qty) },
                    onRemoveItem = { gioHangVM.xoa(it) },
                    onPlaceOrder = { customer, paymentMethod ->
                        gioHangVM.datHang(customer, paymentMethod)
                    }
                )
            }
            composable("history") {
                HistoryScreen(
                    state = historyState,
                    customerId = authState.currentUser?.id ?: "", // Lấy ID của người dùng hiện tại
                    onReload = { customerId -> historyVM.taiHistory(customerId) } // Truyền ID vào hàm reload
                )
            }
        }
    }
}

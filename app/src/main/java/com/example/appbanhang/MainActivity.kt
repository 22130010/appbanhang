package com.example.appbanhang

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.example.appbanhang.ui.layout.AppNavHost
import com.example.appbanhang.ui.viewModel.AuthViewModel
import com.example.appbanhang.ui.viewModel.GioHangViewModel
import com.example.appbanhang.ui.viewModel.HistoryViewModel
import com.example.appbanhang.ui.viewModel.TrangChuViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        val authVM: AuthViewModel by viewModels()
        val homeVM: TrangChuViewModel by viewModels()
        val cartVM: GioHangViewModel by viewModels()
        val historyVM: HistoryViewModel by viewModels()

        setContent {
            AppNavHost(
                authVM = authVM,
                trangChuVM = homeVM,
                gioHangVM = cartVM,
                historyVM = historyVM
            )
        }
    }
}

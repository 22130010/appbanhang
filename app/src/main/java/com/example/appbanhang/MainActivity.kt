package com.example.appbanhang

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.appbanhang.ui.layout.AppNavHost
import com.example.appbanhang.ui.viewModel.GioHangViewModel
import com.example.appbanhang.ui.viewModel.HistoryViewModel
import com.example.appbanhang.ui.viewModel.TrangChuViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val homeVM = TrangChuViewModel()
        val cartVM = GioHangViewModel()
        val historyVM = HistoryViewModel()

        setContent {
            AppNavHost(
                trangChuVM = homeVM,
                gioHangVM = cartVM,
                historyVM = historyVM
            )
        }
    }
}

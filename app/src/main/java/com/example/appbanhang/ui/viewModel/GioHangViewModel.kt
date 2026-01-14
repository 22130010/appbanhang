package com.example.appbanhang.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appbanhang.data.CartItem
import com.example.appbanhang.data.MonAn
import com.example.appbanhang.data.Repository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class GioHangState(
    val cartItems: List<CartItem> = emptyList(),
    val dangDat: Boolean = false,
    val ketQua: String? = null,
    val loi: String? = null
)

class GioHangViewModel : ViewModel() {
    private val _state = MutableStateFlow(GioHangState())
    val state: StateFlow<GioHangState> = _state

    fun them(monAn: MonAn) {
        val current = _state.value.cartItems.toMutableList()
        val idx = current.indexOfFirst { it.monAn.id == monAn.id }
        if (idx >= 0) {
            current[idx].soLuong += 1
        } else {
            current.add(CartItem(monAn, 1))
        }
        _state.update { it.copy(cartItems = current, ketQua = null, loi = null) }
    }

    fun capNhatSoLuong(item: CartItem, soLuong: Int) {
        val current = _state.value.cartItems.map {
            if (it.monAn.id == item.monAn.id) it.copy(soLuong = soLuong) else it
        }.filter { it.soLuong > 0 }
        _state.update { it.copy(cartItems = current) }
    }

    fun xoa(item: CartItem) {
        val current = _state.value.cartItems.filter { it.monAn.id != item.monAn.id }
        _state.update { it.copy(cartItems = current) }
    }

    fun datHang(hoTen: String, sdt: String, diaChi: String, thanhToan: String) {
        viewModelScope.launch {
            _state.update { it.copy(dangDat = true, ketQua = null, loi = null) }
            try {
                val res = Repository.createOrder(_state.value.cartItems, hoTen, sdt, diaChi, thanhToan)
                _state.update { GioHangState(cartItems = emptyList(), dangDat = false, ketQua = "Đặt hàng #${res.id} thành công", loi = null) }
            } catch (e: Exception) {
                _state.update { it.copy(dangDat = false, loi = e.message ?: "Đặt hàng thất bại") }
            }
        }
    }
}

package com.example.appbanhang.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appbanhang.data.CartItem
import com.example.appbanhang.data.MonAn
import com.example.appbanhang.data.Repository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
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
    val state: StateFlow<GioHangState> = _state.asStateFlow()

    fun them(monAn: MonAn) {
        _state.update { currentState ->
            val existingItem = currentState.cartItems.find { it.monAn.id == monAn.id }
            val newItems = if (existingItem != null) {
                currentState.cartItems.map {
                    if (it.monAn.id == monAn.id) it.copy(soLuong = it.soLuong + 1) else it
                }
            } else {
                currentState.cartItems + CartItem(monAn = monAn, soLuong = 1)
            }
            currentState.copy(cartItems = newItems, ketQua = null, loi = null)
        }
    }

    fun xoa(item: CartItem) {
        _state.update { currentState ->
            currentState.copy(cartItems = currentState.cartItems.filterNot { it.monAn.id == item.monAn.id })
        }
    }

    fun capNhatSoLuong(item: CartItem, soLuongMoi: Int) {
        if (soLuongMoi <= 0) {
            xoa(item)
            return
        }
        _state.update { currentState ->
            val newItems = currentState.cartItems.map {
                if (it.monAn.id == item.monAn.id) it.copy(soLuong = soLuongMoi) else it
            }
            currentState.copy(cartItems = newItems)
        }
    }

    fun datHang(customer: Customer, paymentMethod: String) {
        viewModelScope.launch {
            _state.update { it.copy(dangDat = true, ketQua = null, loi = null) }
            val currentItems = _state.value.cartItems
            if (currentItems.isEmpty()) {
                _state.update { it.copy(dangDat = false, loi = "Giỏ hàng trống!") }
                return@launch
            }

            try {
                // Repository giờ làm việc trực tiếp với Firebase
                val orderId = Repository.createOrder(customer, currentItems, paymentMethod)
                _state.update { 
                    it.copy(
                        dangDat = false, 
                        ketQua = "Đặt hàng thành công! Mã đơn: $orderId",
                        cartItems = emptyList() // Xóa giỏ hàng sau khi đặt thành công
                    )
                }
            } catch (e: Exception) {
                _state.update { it.copy(dangDat = false, loi = e.message ?: "Đã xảy ra lỗi không xác định") }
            }
        }
    }
}

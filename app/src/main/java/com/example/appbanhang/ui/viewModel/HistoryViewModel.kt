package com.example.appbanhang.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appbanhang.data.FirebaseOrder
import com.example.appbanhang.data.Repository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class HistoryState(
    val orders: List<FirebaseOrder> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

class HistoryViewModel : ViewModel() {

    private val _state = MutableStateFlow(HistoryState())
    val state: StateFlow<HistoryState> = _state.asStateFlow()

    // THAY ĐỔI: Hàm này giờ nhận customerId
    fun taiHistory(customerId: String) {
        if (customerId.isBlank()) {
            _state.update { it.copy(error = "Không tìm thấy thông tin người dùng.") }
            return
        }

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            try {
                // Gọi Repository với customerId
                val orders = Repository.fetchOrders(customerId)
                _state.update { it.copy(isLoading = false, orders = orders) }
            } catch (e: Exception) {
                _state.update { it.copy(isLoading = false, error = e.message ?: "Lỗi không xác định") }
            }
        }
    }
}

package com.example.appbanhang.ui.viewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appbanhang.data.dto.OrderResponse
import com.example.appbanhang.data.Repository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class HistoryState(
    val orders: List<OrderResponse> = emptyList(),
    val dangTai: Boolean = false,
    val loi: String? = null
)

class HistoryViewModel : ViewModel() {
    private val _state = MutableStateFlow(HistoryState())
    val state: StateFlow<HistoryState> = _state

    init {
        taiHistory()
    }

    fun taiHistory() {
        viewModelScope.launch {
            _state.update { it.copy(dangTai = true, loi = null) }
            try {
                val data = Repository.fetchOrders()
                _state.update { it.copy(orders = data, dangTai = false) }
            } catch (e: Exception) {
                _state.update { it.copy(dangTai = false, loi = e.message ?: "Có lỗi xảy ra") }
            }
        }
    }
}

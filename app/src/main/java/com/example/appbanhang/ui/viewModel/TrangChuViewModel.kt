package com.example.appbanhang.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appbanhang.data.MonAn
import com.example.appbanhang.data.Repository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class TrangChuState(
    val timKiem: String = "",
    val danhSach: List<MonAn> = emptyList(),
    val dangTai: Boolean = false,
    val loi: String? = null
)

class TrangChuViewModel : ViewModel() {
    private val _state = MutableStateFlow(TrangChuState())
    val state: StateFlow<TrangChuState> = _state

    init {
        taiDanhSach()
    }

    fun capNhatTimKiem(q: String) {
        _state.update { it.copy(timKiem = q) }
    }

    fun taiDanhSach() {
        viewModelScope.launch {
            _state.update { it.copy(dangTai = true, loi = null) }
            try {
                val data = Repository.fetchProducts()
                _state.update { it.copy(danhSach = data, dangTai = false) }
            } catch (e: Exception) {
                _state.update { it.copy(dangTai = false, loi = e.message ?: "Có lỗi xảy ra") }
            }
        }
    }
    fun layMonAnTheoId(id: String): MonAn? {
        return _state.value.danhSach.find { it.id == id } }
}

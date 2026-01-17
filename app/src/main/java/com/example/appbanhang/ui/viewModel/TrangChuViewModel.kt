package com.example.appbanhang.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appbanhang.data.MonAn
import com.example.appbanhang.data.Repository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class TrangChuState(
    val timKiem: String = "",
    val danhSach: List<MonAn> = emptyList(),
    val danhSachHienThi: List<MonAn> = emptyList(), // Danh sách được lọc để hiển thị
    val dangTai: Boolean = false,
    val loi: String? = null
)

class TrangChuViewModel : ViewModel() {
    private val _state = MutableStateFlow(TrangChuState())
    val state: StateFlow<TrangChuState> = _state.asStateFlow()

    init {
        taiDanhSach()
    }

    fun capNhatTimKiem(q: String) {
        _state.update { currentState ->
            val danhSachHienThi = if (q.isBlank()) {
                currentState.danhSach
            } else {
                currentState.danhSach.filter { 
                    it.name.contains(q, ignoreCase = true) 
                }
            }
            currentState.copy(timKiem = q, danhSachHienThi = danhSachHienThi)
        }
    }

    fun taiDanhSach() {
        viewModelScope.launch {
            _state.update { it.copy(dangTai = true, loi = null) }
            try {
                // Gọi đến Repository mới
                val danhSachMonAn = Repository.fetchProducts()
                _state.update { 
                    it.copy(
                        danhSach = danhSachMonAn, 
                        danhSachHienThi = danhSachMonAn, // Ban đầu hiển thị tất cả
                        dangTai = false
                    )
                }
            } catch (e: Exception) {
                _state.update { it.copy(dangTai = false, loi = e.message) }
            }
        }
    }
    
    fun layMonAnTheoId(id: String): MonAn? {
        return _state.value.danhSach.find { it.id == id }
    }
}

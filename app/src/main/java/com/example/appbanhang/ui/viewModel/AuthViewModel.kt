package com.example.appbanhang.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

data class AuthState(
    val isAuthenticated: Boolean = false,
    val isLoading: Boolean = false,
    val error: String? = null,
    val currentUser: Customer? = null
)

// Lớp dữ liệu khớp với cấu trúc trong bảng 'customers'
data class Customer(
    var id: String = "",
    val username: String = "",
    val password: String = "",
    val full_name: String = "",
    val phone: String = "",
    val address: String = "",
    val role: String = "USER"
) {
    // Constructor rỗng cần thiết cho Firebase
    constructor() : this("", "", "", "", "", "", "USER")
}

class AuthViewModel : ViewModel() {

    private val database = FirebaseDatabase.getInstance().getReference("customers")
    private val _authState = MutableStateFlow(AuthState())
    val authState: StateFlow<AuthState> = _authState

    fun login(username: String, pass: String) {
        viewModelScope.launch {
            _authState.value = AuthState(isLoading = true)
            try {
                val snapshot = database.orderByChild("username").equalTo(username).get().await()
                if (snapshot.exists()) {
                    var userFound = false
                    for (userSnapshot in snapshot.children) {
                        val customer = userSnapshot.getValue(Customer::class.java)
                        // CẢNH BÁO: So sánh mật khẩu dạng văn bản thuần là không an toàn!
                        if (customer != null && customer.password == pass) {
                            customer.id = userSnapshot.key ?: ""
                            _authState.value = AuthState(isAuthenticated = true, currentUser = customer)
                            userFound = true
                            break
                        }
                    }
                    if (!userFound) {
                        _authState.value = AuthState(error = "Mật khẩu không chính xác.")
                    }
                } else {
                    _authState.value = AuthState(error = "Tên đăng nhập không tồn tại.")
                }
            } catch (e: Exception) {
                _authState.value = AuthState(error = e.message)
            }
        }
    }

    fun signUp(username: String, pass: String, fullName: String, phone: String, address: String) {
        viewModelScope.launch {
            _authState.value = AuthState(isLoading = true)
            try {
                val snapshot = database.orderByChild("username").equalTo(username).get().await()
                if (snapshot.exists()) {
                    _authState.value = AuthState(error = "Tên đăng nhập đã tồn tại.")
                    return@launch
                }
                
                // Bước 1: Tạo ID mới trước
                val newUserId = database.push().key
                if (newUserId == null) {
                    _authState.value = AuthState(error = "Không thể tạo ID người dùng mới.")
                    return@launch
                }

                // Bước 2: Tạo đối tượng Customer với ID đã có
                val newCustomer = Customer(
                    id = newUserId, // Gán ID ngay từ đầu
                    username = username,
                    password = pass, // CẢNH BÁO: Lưu mật khẩu dạng văn bản thuần!
                    full_name = fullName,
                    phone = phone,
                    address = address,
                    role = "USER"
                )

                // Bước 3: Lưu đối tượng hoàn chỉnh lên Firebase
                database.child(newUserId).setValue(newCustomer).await()
                
                // Bước 4: Cập nhật trạng thái thành công
                _authState.value = AuthState(isAuthenticated = true, currentUser = newCustomer)

            } catch (e: Exception) {
                _authState.value = AuthState(error = e.message)
            }
        }
    }

    fun logout() {
        _authState.value = AuthState(isAuthenticated = false)
    }
}

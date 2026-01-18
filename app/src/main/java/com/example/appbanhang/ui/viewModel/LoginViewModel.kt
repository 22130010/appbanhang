package com.example.appbanhang.ui.viewModel
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appbanhang.data.LoginRequest
import com.example.appbanhang.network.ApiClient
import com.example.appbanhang.network.ApiClient.Aapi
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
class LoginViewModel : ViewModel() {

    var result = mutableStateOf("")
        private set

    fun login(username: String, password: String) {
        viewModelScope.launch {
            try {
                val response = ApiClient.Aapi.login(
                    LoginRequest(username, password)
                )

                if (response.isSuccessful) {
                    result.value = response.body() ?: "Lỗi dữ liệu"
                } else {
                    result.value = "Sai tài khoản hoặc mật khẩu"
                }

            } catch (e: Exception) {
                Log.e("LOGIN", "error", e)
                result.value = "Không kết nối được server"
            }
        }
    }
}

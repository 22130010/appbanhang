package com.example.appbanhang.ui.viewModel
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.appbanhang.data.RegisterRequest
import com.example.appbanhang.network.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterViewModel : ViewModel() {

    var result = mutableStateOf("")
        private set

    fun register(username: String, password: String, repassword: String) {

        val request = RegisterRequest(username, password, repassword)

        ApiClient.Aapi.register(request)
            .enqueue(object : Callback<String> {

                override fun onResponse(
                    call: Call<String>,
                    response: Response<String>
                ) {
                    result.value = response.body() ?: ""
                }

                override fun onFailure(call: Call<String>, t: Throwable) {
                    result.value = "Lỗi kết nối server"
                }
            })
    }
}

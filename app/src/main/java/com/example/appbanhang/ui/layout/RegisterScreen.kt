package com.example.appbanhang.ui.layout

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.appbanhang.ui.viewModel.AuthState

@Composable
fun RegisterScreen(
    authState: AuthState,
    onRegister: (String, String, String, String, String) -> Unit,
    onNavigateToLogin: () -> Unit
) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var fullName by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()), // Thêm cuộn để vừa trên màn hình nhỏ
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Đăng Ký", style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(24.dp))

        OutlinedTextField(value = fullName, onValueChange = { fullName = it }, label = { Text("Họ và tên") }, modifier = Modifier.fillMaxWidth())
        Spacer(Modifier.height(8.dp))

        OutlinedTextField(value = username, onValueChange = { username = it }, label = { Text("Tên đăng nhập") }, modifier = Modifier.fillMaxWidth())
        Spacer(Modifier.height(8.dp))

        OutlinedTextField(value = phone, onValueChange = { phone = it }, label = { Text("Số điện thoại") }, modifier = Modifier.fillMaxWidth())
        Spacer(Modifier.height(8.dp))

        OutlinedTextField(value = address, onValueChange = { address = it }, label = { Text("Địa chỉ") }, modifier = Modifier.fillMaxWidth())
        Spacer(Modifier.height(8.dp))
        
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Mật khẩu") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation()
        )
        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            label = { Text("Xác nhận mật khẩu") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation(),
            isError = password != confirmPassword
        )
        Spacer(Modifier.height(16.dp))

        val canRegister = username.isNotBlank() && password.isNotBlank() && 
                          password == confirmPassword && fullName.isNotBlank() &&
                          phone.isNotBlank() && address.isNotBlank()

        if (authState.isLoading) {
            CircularProgressIndicator()
        } else {
            Button(
                onClick = { onRegister(username, password, fullName, phone, address) },
                modifier = Modifier.fillMaxWidth(),
                enabled = canRegister
            ) {
                Text("Đăng Ký")
            }
        }

        authState.error?.let {
            Text(
                text = it,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        Spacer(Modifier.height(16.dp))
        TextButton(onClick = onNavigateToLogin) {
            Text("Đã có tài khoản? Đăng nhập")
        }
    }
}

package com.example.appbanhang.data

data class MonAn(
    val id: String,
    val ten: String,
    val gia: Int,
    val moTa: String? = null,
    val hinhAnh: String? = null
)

data class CartItem(val monAn: MonAn, var soLuong: Int)

data class DonHang(
    val id: String,
    val danhSach: List<CartItem>,
    val hoTen: String,
    val soDienThoai: String,
    val diaChi: String,
    val tongTien: Int,
    val thanhToan: String
)
data class LoginRequest(
    val username: String,
    val password: String
)

data class RegisterRequest(
    val username: String,
    val password: String,
    val repassword: String
)

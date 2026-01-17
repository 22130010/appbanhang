package com.example.appbanhang.data

data class MonAn(
    var id: String = "", // Dùng để lưu key từ Firebase (ví dụ: "prod1")
    val name: String = "",
    val price: Int = 0,
    val description: String? = null,
    val image_url: String? = null,
    val category_id: String = ""
) {
    // Thêm một hàm khởi tạo trống để Firebase có thể tái tạo đối tượng từ dữ liệu
    constructor() : this("", "", 0, null, null, "")
}


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

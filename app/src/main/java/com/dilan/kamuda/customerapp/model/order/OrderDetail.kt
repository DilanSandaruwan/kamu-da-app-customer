package com.dilan.kamuda.customerapp.model.order

import com.google.gson.annotations.SerializedName

data class OrderDetail(
    @SerializedName("orderId") val orderId: Int,
    @SerializedName("createdAt") val createdAt: String,
    @SerializedName("meal") val meal: String,
    @SerializedName("status") val status: String,
    @SerializedName("total") val total: Double,
    @SerializedName("items") val items: List<OrderItem>,
)

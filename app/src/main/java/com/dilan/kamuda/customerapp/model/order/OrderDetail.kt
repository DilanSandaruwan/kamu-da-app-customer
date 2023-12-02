package com.dilan.kamuda.customerapp.model.order

import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.util.Date

data class OrderDetail(
    @SerializedName("id") val id: Int = -1,
    @SerializedName("custId") val custId: Int,
    @SerializedName("total") val total: Double,
    @SerializedName("date") val date: String,
    @SerializedName("status") val status: String,
    @SerializedName("createdAt") val createdAt: String,
    @SerializedName("items") val items: List<OrderItem>,
):Serializable

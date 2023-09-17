package com.dilan.kamuda.customerapp.model.order

import com.google.gson.annotations.SerializedName

data class OrderItem(
    @SerializedName("name") val name: String,
    @SerializedName("price") val price: Double,
    @SerializedName("quantity") var quantity: Int,
)

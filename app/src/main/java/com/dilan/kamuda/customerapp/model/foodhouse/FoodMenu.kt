package com.dilan.kamuda.customerapp.model.foodhouse

import com.google.gson.annotations.SerializedName

data class FoodMenu(
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("price") val price: Double,
    @SerializedName("status") val status: Boolean,
    var itemCount: Int = 0,
)

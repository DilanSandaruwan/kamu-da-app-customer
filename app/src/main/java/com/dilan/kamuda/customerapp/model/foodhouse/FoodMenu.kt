package com.dilan.kamuda.customerapp.model.foodhouse

import com.google.gson.annotations.SerializedName

data class FoodMenu(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("price") val price: Double,
)

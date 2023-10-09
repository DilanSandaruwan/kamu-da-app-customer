package com.dilan.kamuda.customerapp.model.foodhouse

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class FoodMenu(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("price") val price: Double,
    @SerializedName("status") val status: Boolean,
    @SerializedName("image") val image: String?,
) : Serializable

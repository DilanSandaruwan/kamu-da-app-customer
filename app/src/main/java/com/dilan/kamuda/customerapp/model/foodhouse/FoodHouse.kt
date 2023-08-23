package com.dilan.kamuda.customerapp.model.foodhouse

import com.google.gson.annotations.SerializedName

data class FoodHouse(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("address") val address: String,
    @SerializedName("hotline") val hotline: String,
    @SerializedName("meal") val meal: String,
    @SerializedName("menu") val menu: ArrayList<FoodMenu>,
    @SerializedName("time_out_at") val timeOutAt: String,
    @SerializedName("time_start_at") val timeStartAt: String,
    @SerializedName("ready_at") val readyAt: String,
)
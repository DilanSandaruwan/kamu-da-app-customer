package com.dilan.kamuda.customerapp.model.order

import com.google.gson.annotations.SerializedName

data class OrderDetail(
    @SerializedName("foodhouse_id")val foodHouseId:Int,
    @SerializedName("cust_id")val custId:Int,
    @SerializedName("order_time")val orderTime:String,
    @SerializedName("meal")val meal:String,
    @SerializedName("cust_address")val custAddress:String,
    @SerializedName("status")val status:String,
    @SerializedName("total")val total:Double,
    @SerializedName("order_items")val orderItems:ArrayList<OrderItem>,
)

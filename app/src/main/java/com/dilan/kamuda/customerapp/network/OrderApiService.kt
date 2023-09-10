package com.dilan.kamuda.customerapp.network

import com.dilan.kamuda.customerapp.constant.NetworkConstant
import com.dilan.kamuda.customerapp.model.foodhouse.FoodMenu
import com.dilan.kamuda.customerapp.model.order.OrderDetail
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST

interface OrderApiService {
    @GET(NetworkConstant.ENDPOINT_MENU)
    suspend fun getMenuListForMeal(): Response<List<FoodMenu>>

    @GET(NetworkConstant.ENDPOINT_ORDERS)
    suspend fun getOrdersList(): Response<List<OrderDetail>>

    @POST(NetworkConstant.ENDPOINT_SAVE_ORDER)
    suspend fun placeOrderInStore(myOrder: OrderDetail): Response<Boolean>
}
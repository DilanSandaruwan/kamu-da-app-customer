package com.dilan.kamuda.customerapp.network

import com.dilan.kamuda.customerapp.constant.NetworkConstant
import com.dilan.kamuda.customerapp.model.foodhouse.FoodMenu
import retrofit2.Response
import retrofit2.http.GET

interface OrderApiService {
    @GET(NetworkConstant.ENDPOINT_MENU)
    suspend fun getMenuListForMeal(): Response<List<FoodMenu>>
}
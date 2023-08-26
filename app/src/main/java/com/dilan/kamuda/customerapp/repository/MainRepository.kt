package com.dilan.kamuda.customerapp.repository

import com.dilan.kamuda.customerapp.model.foodhouse.FoodMenu
import com.dilan.kamuda.customerapp.network.OnBoardingApiService
import com.dilan.kamuda.customerapp.network.OrderApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MainRepository @Inject constructor(
    private val onBoardingApiService: OnBoardingApiService,
    private val orderApiService: OrderApiService,
    ) {

    suspend fun getMenuListForMealFromDataSource(): List<FoodMenu>? {
        return withContext(Dispatchers.IO) {
            return@withContext getResponseFromRemoteService()
        }
    }

    private suspend fun getResponseFromRemoteService(): List<FoodMenu>? {
        val response = orderApiService.getMenuListForMeal()
        if (response.isSuccessful) {
            return response.body()
        }
        return emptyList()
    }
}
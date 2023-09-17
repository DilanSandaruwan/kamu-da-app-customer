package com.dilan.kamuda.customerapp.repository

import android.util.Log
import com.dilan.kamuda.customerapp.model.foodhouse.FoodMenu
import com.dilan.kamuda.customerapp.model.order.OrderDetail
import com.dilan.kamuda.customerapp.network.OnBoardingApiService
import com.dilan.kamuda.customerapp.network.OrderApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MainRepository @Inject constructor(
    private val onBoardingApiService: OnBoardingApiService,
    private val orderApiService: OrderApiService,
) {
    private val TAG = "MainRepository"
    suspend fun getMenuListForMealFromDataSource(): List<FoodMenu>? {
        return withContext(Dispatchers.IO) {
            return@withContext getResponseFromRemoteService()
        }
    }

    suspend fun getOrderListFromDataSource(id: Int): List<OrderDetail>? {
        return withContext(Dispatchers.IO) {
            return@withContext getOrderListResponseFromRemoteService(id)
        }
    }

    private suspend fun getResponseFromRemoteService(): List<FoodMenu>? {
        val response = orderApiService.getMenuListForMeal()
        if (response.isSuccessful) {
            return response.body()
        }
        return emptyList()
    }

    private suspend fun getOrderListResponseFromRemoteService(id: Int): List<OrderDetail>? {
        try {
            val response = orderApiService.getOrdersList(id)
            if (response.isSuccessful) {
                Log.e(TAG, "getOrderListResponseFromRemoteService: ${response.body()}", )
                return response.body()
            }
            return emptyList()
        } catch (e: Exception) {
            Log.e(TAG, "getOrderListResponseFromRemoteService: ${e.message}")
            return emptyList()
        }

    }

    suspend fun placeOrderInDataSource(myOrder: OrderDetail): OrderDetail? {
        return withContext(Dispatchers.IO) {
            return@withContext placeOrderInRemoteService(myOrder)
        }
    }

    private suspend fun placeOrderInRemoteService(myOrder: OrderDetail): OrderDetail? {
        val response = orderApiService.placeOrderInStore(myOrder)
        if (response.isSuccessful) {
            Log.e(TAG, "placeOrderInRemoteService: success: ${response.body()}", )
            return response.body()
//            return response.body()
        }
        Log.e(TAG, "placeOrderInRemoteService: failure: ${response.body()}", )
        return null
    }

}
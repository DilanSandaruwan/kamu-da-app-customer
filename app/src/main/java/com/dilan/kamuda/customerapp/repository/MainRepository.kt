package com.dilan.kamuda.customerapp.repository

import android.util.Log
import com.dilan.kamuda.customerapp.model.foodhouse.FoodHouse
import com.dilan.kamuda.customerapp.model.foodhouse.FoodMenu
import com.dilan.kamuda.customerapp.model.order.OrderDetail
import com.dilan.kamuda.customerapp.network.OnBoardingApiService
import com.dilan.kamuda.customerapp.network.OrderApiService
import com.dilan.kamuda.customerapp.network.utils.ApiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MainRepository @Inject constructor(
    private val onBoardingApiService: OnBoardingApiService,
    private val orderApiService: OrderApiService,
) {
    private val TAG = "MainRepository"
    suspend fun getMenuListForMealFromDataSource(): ApiState<List<FoodMenu>> {
        return withContext(Dispatchers.IO) {
            return@withContext getResponseFromRemoteService()
        }
    }

    private suspend fun getResponseFromRemoteService(): ApiState<List<FoodMenu>> {
        return try {
            val response = orderApiService.getMenuListForMeal()
            if (response.isSuccessful) {
                Log.e(TAG, "getResponseFromRemoteService: ${response.message()}")
                ApiState.Success(response.body() ?: emptyList())
            } else {
                Log.e(TAG, "getResponseFromRemoteService: ${response.message()}")
                ApiState.Failure("Error in fetching menu list")
            }
        } catch (exception: Exception) {
            Log.e(TAG, "getResponseFromRemoteService: ${exception.toString()}")
            ApiState.Failure(exception.message.toString())
        }
    }

    suspend fun getOrderListFromDataSource(id: Int): ApiState<List<OrderDetail>?> {
        return withContext(Dispatchers.IO) {
            return@withContext getOrderListResponseFromRemoteService(id)
        }
    }


    private suspend fun getOrderListResponseFromRemoteService(id: Int): ApiState<List<OrderDetail>?> {
        return try {
            val response = orderApiService.getOrdersList(id)
            if (response.isSuccessful) {
                Log.e(TAG, "getOrderListResponseFromRemoteService: ${response.body()}")
                ApiState.Success(response.body())
            } else ApiState.Failure(response.message())
        } catch (e: Exception) {
            Log.e(TAG, "getOrderListResponseFromRemoteService: ${e.message}")
            ApiState.Failure(e.message.toString())
        }

    }

    suspend fun placeOrderInDataSource(myOrder: OrderDetail): ApiState<OrderDetail?> {
        return withContext(Dispatchers.IO) {
            return@withContext placeOrderInRemoteService(myOrder)
        }
    }

    private suspend fun placeOrderInRemoteService(myOrder: OrderDetail): ApiState<OrderDetail?> {
        return try {
            val response = orderApiService.placeOrderInStore(myOrder)
            if (response.isSuccessful) {
                Log.e(TAG, "placeOrderInRemoteService: success: ${response.body()}")
                ApiState.Success(response.body())
            } else {
                Log.e(TAG, "placeOrderInRemoteService: success: ${response.body()}")
                ApiState.Failure("Response failed")
            }
        } catch (exception: Exception) {
            Log.e(TAG, "placeOrderInRemoteService: failure: ${exception.message}")
            ApiState.Failure(exception.message.toString())
        }
    }

    suspend fun getFoodHouseDetailsFromDataSource(): FoodHouse? {
        return withContext(Dispatchers.IO) {
            return@withContext getFoodHouseDetailsFromRemoteSource()
        }
    }

    private suspend fun getFoodHouseDetailsFromRemoteSource(): FoodHouse? {
        val response = onBoardingApiService.getFoodHouse()
        if (response.isSuccessful) {
            return response.body()
        }
        return null
    }

    /***
     * UPDATE the order status
     */
    suspend fun updateOrderByIdWithStatusOnDataSource(orderId: Int, status: String): ApiState<OrderDetail?> {
        return withContext(Dispatchers.IO) {
            return@withContext (updateOrderByIdWithStatusOnRemoteSource(orderId, status))
        }
    }

    private suspend fun updateOrderByIdWithStatusOnRemoteSource(
        orderId: Int,
        status: String
    ): ApiState<OrderDetail?> {
        return try {
            val response = orderApiService.updateOrderByIdWithStatus(orderId, status)
            if (response.isSuccessful) {
                ApiState.Success(response.body())
            } else {
                ApiState.Failure(response.errorBody().toString())
            }
        } catch (exception:Exception){
            ApiState.Failure(exception.message.toString())
        }
    }

}
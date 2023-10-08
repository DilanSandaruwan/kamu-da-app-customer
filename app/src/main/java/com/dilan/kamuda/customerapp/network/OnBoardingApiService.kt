package com.dilan.kamuda.customerapp.network

import com.dilan.kamuda.customerapp.constant.NetworkConstant
import com.dilan.kamuda.customerapp.model.foodhouse.FoodHouse
import com.dilan.kamuda.customerapp.model.specific.KamuDaResponseObject
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST

interface OnBoardingApiService {

    @POST(NetworkConstant.ENDPOINT_2FA)
    suspend fun postLoginEmailForOtp(): Response<KamuDaResponseObject>

    @POST(NetworkConstant.ENDPOINT_VERIFY_OTP)
    suspend fun postLoginOtpVerify(): Response<KamuDaResponseObject>

    @POST(NetworkConstant.ENDPOINT_SIGNUP)
    suspend fun postSignupDetails(): Response<KamuDaResponseObject>

    @GET(NetworkConstant.ENDPOINT_FOODHOUSE)
    suspend fun getFoodHouse(): Response<FoodHouse>

}
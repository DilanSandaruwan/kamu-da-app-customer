package com.dilan.kamuda.customerapp.repository

import com.dilan.kamuda.customerapp.network.OnBoardingApiService
import javax.inject.Inject

/***
 * TODO
 * Use as the main repository for API calls and local db calls
 */
class MainRepository @Inject constructor(
    private val onBoardingApiService: OnBoardingApiService
) {

}
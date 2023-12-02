package com.dilan.kamuda.customerapp.viewmodels.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.dilan.kamuda.customerapp.model.foodhouse.FoodHouse
import com.dilan.kamuda.customerapp.model.order.OrderDetail
import com.dilan.kamuda.customerapp.network.utils.ApiState
import com.dilan.kamuda.customerapp.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val mainRepository: MainRepository,
    application: Application
) : AndroidViewModel(application) {

    private val _foodHouseDetail = MutableLiveData<FoodHouse?>()
    val foodHouseDetail: LiveData<FoodHouse?>
        get() = _foodHouseDetail

    private val _ordersList = MutableLiveData<List<OrderDetail>>()
    val ordersList: LiveData<List<OrderDetail>>
        get() = _ordersList

    private val _showLoader = MutableLiveData<Boolean>()
    val showLoader: LiveData<Boolean> = _showLoader

    private val _showErrorPage = MutableLiveData<Boolean>()
    val showErrorPage: LiveData<Boolean> = _showErrorPage


    fun getFoodHouseDetails() {
        //_showErrorPage.value = false
        //_showLoader.value = true
        viewModelScope.launch {

            when (val res = mainRepository.getFoodHouseDetailsFromDataSource()) {
                is ApiState.Success -> {
                    _foodHouseDetail.postValue(res.data!!)
                    //_showLoader.postValue(false)
                }

                is ApiState.Failure -> {
                    _foodHouseDetail.postValue(null)
                    //_showLoader.postValue(false)
                    //_showErrorPage.postValue(true)
                }

                is ApiState.Loading -> {

                }
            }
        }
    }

    init {

    }
}
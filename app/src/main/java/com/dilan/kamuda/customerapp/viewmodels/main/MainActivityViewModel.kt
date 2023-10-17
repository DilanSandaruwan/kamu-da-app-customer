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

    private lateinit var food_house_details: FoodHouse
    private val _ordersList = MutableLiveData<List<OrderDetail>>()
    val ordersList: LiveData<List<OrderDetail>>
        get() = _ordersList

    private val _latestOrder = MutableLiveData<OrderDetail?>()
    val latestOrder: LiveData<OrderDetail?>
        get() = _latestOrder


    private val _showLoader = MutableLiveData<Boolean>()
    val showLoader: LiveData<Boolean> = _showLoader

    private fun getFoodHouseDetails() {
        viewModelScope.launch {
            val foodhouse = mainRepository.getFoodHouseDetailsFromDataSource()
            if (foodhouse == null) {
                //TODO(Show an error message to Try again shortly)
            } else {
                food_house_details = foodhouse
            }
        }
    }

    fun getLatestOrderOfCustomer(custId: Int) {
        viewModelScope.launch {
            _showLoader.postValue(true)
            val res = mainRepository.getOrderListFromDataSource(custId)
            when (res) {
                is ApiState.Success -> {
                    _showLoader.postValue(false)
                    if (res.data != null) {
                        _latestOrder.postValue(res.data.maxByOrNull {
                            it.id
                        })
                    } else {
                        _latestOrder.postValue(null)
                    }
                }

                is ApiState.Failure -> {
                    _showLoader.postValue(false)
                }

                is ApiState.Loading -> {
                    _showLoader.postValue(true)
                }
            }
        }
    }

    init {
        //getFoodHouseDetails()
    }
}
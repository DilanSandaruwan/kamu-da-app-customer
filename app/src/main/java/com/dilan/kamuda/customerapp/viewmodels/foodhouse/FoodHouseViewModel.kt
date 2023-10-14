package com.dilan.kamuda.customerapp.viewmodels.foodhouse

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.dilan.kamuda.customerapp.model.foodhouse.FoodMenu
import com.dilan.kamuda.customerapp.model.order.OrderDetail
import com.dilan.kamuda.customerapp.model.specific.KamuDaPopup
import com.dilan.kamuda.customerapp.network.utils.ApiState
import com.dilan.kamuda.customerapp.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FoodHouseViewModel @Inject constructor(
    private val mainRepository: MainRepository,
    application: Application,
) : AndroidViewModel(application) {

    private val _menuList = MutableLiveData<List<FoodMenu>>()
    val menuList: LiveData<List<FoodMenu>>
        get() = _menuList

    private val _latestOrder = MutableLiveData<OrderDetail?>()
    val latestOrder: LiveData<OrderDetail?>
        get() = _latestOrder

    private val _showLoader = MutableLiveData<Boolean>()
    val showLoader: LiveData<Boolean> = _showLoader

    private val _showErrorPopup = MutableLiveData<KamuDaPopup>()
    val showErrorPopup: LiveData<KamuDaPopup> = _showErrorPopup


    fun getMenuListForAll() {
        _showLoader.value = true
        viewModelScope.launch {

            when (val res = mainRepository.getMenuListForMealFromDataSource()) {
                is ApiState.Success -> {
                    _showLoader.postValue(false)
                    _menuList.postValue(res.data ?: emptyList())
                }

                is ApiState.Failure -> {
                    val kamuDaPopup = KamuDaPopup(
                        "Error",
                        "Connection Failed. Try Again!",
                        "",
                        "Close",
                        2
                    )
                    _showErrorPopup.postValue(kamuDaPopup)
                    _showLoader.postValue(false)
                    _menuList.postValue(emptyList())
                }

                is ApiState.Loading -> {
                    _showLoader.postValue(true)
                }
            }

        }
    }

    fun getLatestOrderOfCustomer(custId: Int) {
        viewModelScope.launch {
            //_showLoader.postValue(true)
            val res = mainRepository.getOrderListFromDataSource(custId)
            when (res) {
                is ApiState.Success -> {
                    // _showLoader.postValue(false)
                    if (res.data != null) {
                        _latestOrder.postValue(res.data.maxByOrNull {
                            it.id
                        })
                    } else {
                        _latestOrder.postValue(null)
                    }
                }

                is ApiState.Failure -> {
                    //_showLoader.postValue(false)
                }

                is ApiState.Loading -> {
                    //_showLoader.postValue(true)
                }
            }
        }
    }

    init {
        getMenuListForAll()
    }
}
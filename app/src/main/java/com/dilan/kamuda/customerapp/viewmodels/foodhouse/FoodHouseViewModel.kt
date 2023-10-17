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

    private val _showErrorPage = MutableLiveData<Boolean>()
    val showErrorPage: LiveData<Boolean> = _showErrorPage

    private val _successfulRetrieve = MutableLiveData<Boolean>()
    val successfulRetrieve: LiveData<Boolean> = _successfulRetrieve


    fun getMenuListForAll() {

        _showErrorPage.value = false
        _showLoader.value = true
        _successfulRetrieve.value = false

        viewModelScope.launch {

            when (val res = mainRepository.getMenuListForMealFromDataSource()) {
                is ApiState.Success -> {
                    _showLoader.postValue(false)
                    _menuList.postValue(res.data!!)
                    _successfulRetrieve.postValue(true)
                }

                is ApiState.Failure -> {
                    _showLoader.postValue(false)
                    _showErrorPage.postValue(true)
                }

                is ApiState.Loading -> {

                }
            }

        }
    }

    fun getLatestOrderOfCustomer(custId: Int) {

        _showErrorPage.value = false

        viewModelScope.launch {

            when (val res = mainRepository.getOrderListFromDataSource(custId)) {
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
                    _showErrorPage.postValue(true)
                }

                is ApiState.Loading -> {

                }
            }
        }
    }
}
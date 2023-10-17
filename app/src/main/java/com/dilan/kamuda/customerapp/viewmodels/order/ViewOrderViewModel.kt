package com.dilan.kamuda.customerapp.viewmodels.order

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.dilan.kamuda.customerapp.model.order.OrderDetail
import com.dilan.kamuda.customerapp.model.specific.KamuDaPopup
import com.dilan.kamuda.customerapp.network.utils.ApiState
import com.dilan.kamuda.customerapp.repository.MainRepository
import com.dilan.kamuda.customerapp.util.KamuDaSecurePreference
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ViewOrderViewModel @Inject constructor(
    private val mainRepository: MainRepository,
    private val kamuDaSecurePreference: KamuDaSecurePreference,
    application: Application
) : AndroidViewModel(application) {

    var currentlySelectedGroup = "ongoing"
    val ongoingList = MutableLiveData<List<OrderDetail>>()
    val pastOrdersList = MutableLiveData<List<OrderDetail>>()

    private val _ordersList = MutableLiveData<List<OrderDetail>>()
    val ordersList: LiveData<List<OrderDetail>>
        get() = _ordersList

    private val _objectHasUpdated = MutableLiveData<OrderDetail?>()
    val objectHasUpdated: LiveData<OrderDetail?>
        get() = _objectHasUpdated

    private val _showLoader = MutableLiveData<Boolean>()
    val showLoader: LiveData<Boolean> = _showLoader

    private val _showErrorPopup = MutableLiveData<KamuDaPopup>()
    val showErrorPopup: LiveData<KamuDaPopup> = _showErrorPopup

    fun getOrdersListOfCustomer(custId: Int) {
        viewModelScope.launch {
            _showLoader.postValue(true)
            when (val res = mainRepository.getOrderListFromDataSource(custId)) {
                is ApiState.Success -> {
                    _showLoader.postValue(false)
                    if (res.data != null) {
                        _ordersList.postValue(res.data!!)
                    } else {
                        _ordersList.postValue(emptyList())
                    }
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
                }

                is ApiState.Loading -> {
                    _showLoader.postValue(true)
                }
            }
        }
    }

    fun updateOrderWithStatus(orderId: Int, status: String) {
        viewModelScope.launch {
            _showLoader.postValue(true)
            val response = mainRepository.updateOrderByIdWithStatusOnDataSource(orderId, status)
            when (response) {
                is ApiState.Success -> {
                    _showLoader.postValue(false)
                    _objectHasUpdated.postValue(response.data)
                }

                is ApiState.Failure -> {
                    _objectHasUpdated.postValue(null)
                    _showLoader.postValue(false)
                }

                is ApiState.Loading -> {
                    _showLoader.postValue(true)
                }
            }
        }
    }

    init {
        //getOrdersListOfCustomer()
    }
}
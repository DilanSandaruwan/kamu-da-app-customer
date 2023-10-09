package com.dilan.kamuda.customerapp.viewmodels.order

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.dilan.kamuda.customerapp.model.order.OrderDetail
import com.dilan.kamuda.customerapp.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ViewOrderViewModel @Inject constructor(
    private val mainRepository: MainRepository,
    application: Application
) : AndroidViewModel(application) {

    var currentlySelectedGroup = "ongoing"
    val ongoingList = MutableLiveData<List<OrderDetail>>()
    val pastOrdersList = MutableLiveData<List<OrderDetail>>()

    private val _ordersList = MutableLiveData<List<OrderDetail>>()
    val ordersList: LiveData<List<OrderDetail>>
        get() = _ordersList

    private fun getOrdersListOfCustomer(custId: Int) {
        viewModelScope.launch {
            _ordersList.postValue(mainRepository.getOrderListFromDataSource(custId))
        }
    }

    private fun getOrdersList() {

//        val orderItems = listOf(
//            OrderItem( "Burger", 8.99,2 ),
//            OrderItem( "Fries", 3.99,1 ),
//            OrderItem("Soda", 1.99,3 ),
//        )
//
//        val orderDetail = OrderDetail(
//            orderId = 12345,
//            createdAt = "2023-08-28T12:34:56Z",
//            meal = "Lunch",
//            status = "Processing",
//            total = 20.96,
//            items = orderItems
//        )
//
//        _ordersList.value = listOf(orderDetail)

    }

    init {
        getOrdersListOfCustomer(12)
    }
}
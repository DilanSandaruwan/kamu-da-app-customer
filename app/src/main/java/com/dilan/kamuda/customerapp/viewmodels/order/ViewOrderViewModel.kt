package com.dilan.kamuda.customerapp.viewmodels.order

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.dilan.kamuda.customerapp.model.order.OrderDetail
import com.dilan.kamuda.customerapp.model.order.OrderItem
import com.dilan.kamuda.customerapp.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ViewOrderViewModel @Inject constructor(
    private val mainRepository: MainRepository,
    application: Application
) : AndroidViewModel(application) {

    private val _ordersList = MutableLiveData<List<OrderDetail>>()
    val ordersList : LiveData<List<OrderDetail>>
        get() = _ordersList

    private fun getOrdersList(){

        val orderItems = listOf(
            OrderItem(1, "Burger", 8.99,2 ),
            OrderItem(2, "Fries", 3.99,1 ),
            OrderItem(3, "Soda", 1.99,3 ),
        )

        val orderDetail = OrderDetail(
            orderId = 12345,
            createdAt = "2023-08-28T12:34:56Z",
            meal = "Lunch",
            status = "Processing",
            total = 20.96,
            items = orderItems
        )

        _ordersList.value = listOf(orderDetail)
//        viewModelScope.launch {
//            _ordersList.value = mainRepository.getOrderListFromDataSource()
//        }
    }

    init{
        getOrdersList()
    }
}
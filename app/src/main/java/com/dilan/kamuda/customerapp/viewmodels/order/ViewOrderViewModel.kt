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

    private val _objectHasUpdated = MutableLiveData<OrderDetail?>()
    val objectHasUpdated: LiveData<OrderDetail?>
        get() = _objectHasUpdated

    private val _resetList = MutableLiveData<Boolean>(false)
    val resetList: LiveData<Boolean>
        get() = _resetList

    fun getOrdersListOfCustomer(custId: Int) {
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

//    fun saveData(myOrder: OrderDetail) {
//        Log.e("Orders", "saveData: $myOrder")
//        viewModelScope.launch {
//            val res = mainRepository.placeOrderInDataSource(myOrder)
//            if (res != null) {
//                _resetList.postValue(true)
//            }
//        }
//    }

    fun updateOrderWithStatus(orderId: Int, status: String) {
        viewModelScope.launch {
            val response = mainRepository.updateOrderByIdWithStatusOnDataSource(orderId, status)
            _objectHasUpdated.postValue(response)
        }
    }

    init {
        getOrdersListOfCustomer(12)
    }
}
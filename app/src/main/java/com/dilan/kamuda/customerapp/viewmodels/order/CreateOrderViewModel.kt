package com.dilan.kamuda.customerapp.viewmodels.order

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.dilan.kamuda.customerapp.model.foodhouse.FoodMenu
import com.dilan.kamuda.customerapp.model.order.OrderDetail
import com.dilan.kamuda.customerapp.model.order.OrderItem
import com.dilan.kamuda.customerapp.model.order.OrderItemIntermediate
import com.dilan.kamuda.customerapp.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateOrderViewModel @Inject constructor(
    private val mainRepository: MainRepository,
    application: Application
) : AndroidViewModel(application) {

    private val _menuList = MutableLiveData<List<OrderItemIntermediate>>()
    val menuList: LiveData<List<OrderItemIntermediate>>
        get() = _menuList

    private val _checkedItems = MutableLiveData<List<OrderItemIntermediate>>()
    val checkedItems: LiveData<List<OrderItemIntermediate>>
        get() = _checkedItems

    private val _emptyOrder = MutableLiveData<Boolean>(true)
    val emptyOrder: LiveData<Boolean>
        get() = _emptyOrder

    private val _totalAmount = MutableLiveData<Boolean>(false)
    val totalAmount: LiveData<Boolean>
        get() = _totalAmount

    private val _resetList = MutableLiveData<Boolean>(false)
    val resetList: LiveData<Boolean>
        get() = _resetList


    fun getMenuListForMeal(meal: String) {
        viewModelScope.launch {

            var list = mainRepository.getMenuListForMealFromDataSource() ?: emptyList()

            convertToOrderItemsIntermediate(list)

        }
    }

    private fun convertToOrderItemsIntermediate(foodMenus: List<FoodMenu>) {
        var list = foodMenus.map { foodMenu ->
            OrderItemIntermediate(
                foodMenu.name,
                foodMenu.price,
                0
            ) // Assuming quantity is 0 for each item
        }
        if (list.isEmpty()) {
            _menuList.postValue(list)
        } else {
            _menuList.postValue(list)
        }
    }

    fun setCheckedItemsList(updatedCheckedItems: MutableList<OrderItemIntermediate>) {
        _emptyOrder.value = updatedCheckedItems.size < 1
        _checkedItems.value = updatedCheckedItems
    }

    private fun placeOrder() {

    }

    fun saveData(myOrder: OrderDetail) {
        Log.e("Orders", "saveData: $myOrder")
        viewModelScope.launch {
            val res = mainRepository.placeOrderInDataSource(myOrder)
            if(res!=null){
                _resetList.postValue(true)
            }
        }
    }

    fun calculateTotal() {
        _totalAmount.value = true
    }

    init {
        getMenuListForMeal("breakfast")
    }
}
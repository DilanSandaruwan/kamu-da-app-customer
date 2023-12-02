package com.dilan.kamuda.customerapp.viewmodels.order

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.dilan.kamuda.customerapp.model.foodhouse.FoodMenu
import com.dilan.kamuda.customerapp.model.order.OrderDetail
import com.dilan.kamuda.customerapp.model.order.OrderItemIntermediate
import com.dilan.kamuda.customerapp.model.specific.KamuDaPopup
import com.dilan.kamuda.customerapp.network.utils.ApiState
import com.dilan.kamuda.customerapp.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReorderViewModel @Inject constructor(
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

    private val _savedSuccessfully = MutableLiveData<Boolean>(false)
    val savedSuccessfully: LiveData<Boolean>
        get() = _savedSuccessfully

    private val _showLoader = MutableLiveData<Boolean>()
    val showLoader: LiveData<Boolean> = _showLoader

    private val _showErrorPopup = MutableLiveData<KamuDaPopup>()
    val showErrorPopup: LiveData<KamuDaPopup> = _showErrorPopup


    fun getMenuListForMeal() {
        viewModelScope.launch {

            var res = mainRepository.getMenuListForMealFromDataSource()
            when (res) {
                is ApiState.Success -> {
                    convertToOrderItemsIntermediate(res.data)
                }

                is ApiState.Failure -> {
                    val kamuDaPopup = KamuDaPopup(
                        "Error",
                        "Failed to load the menu list",
                        "",
                        "Close",
                        2
                    )
                    _showErrorPopup.postValue(kamuDaPopup)
                    convertToOrderItemsIntermediate(emptyList())
                }

                is ApiState.Loading -> {

                }
            }

        }
    }

    private fun convertToOrderItemsIntermediate(foodMenus: List<FoodMenu>) {

        var modifiedResponseBody = mutableListOf<OrderItemIntermediate>()
        var originalResponseBody = foodMenus
        var bitmap: ByteArray?
        originalResponseBody.let {
            for (item in originalResponseBody) {

                bitmap = if (item.image != null) {
                    val imageData =
                        android.util.Base64.decode(item.image, android.util.Base64.DEFAULT)
                    imageData
                    //BitmapFactory.decodeByteArray(imageData,0,imageData.size)
                } else {
                    null
                }
                modifiedResponseBody.add(
                    OrderItemIntermediate(item.name, item.price, 0, bitmap)
                )
            }
        }


        if (modifiedResponseBody.isEmpty()) {
            _menuList.postValue(modifiedResponseBody)
        } else {
            _menuList.postValue(modifiedResponseBody)
        }
    }

    fun setCheckedItemsList(updatedCheckedItems: MutableList<OrderItemIntermediate>) {
        _emptyOrder.value = updatedCheckedItems.size < 1
        _checkedItems.value = updatedCheckedItems
    }

    fun saveData(myOrder: OrderDetail) {
        Log.e("Orders", "saveData: $myOrder")
        viewModelScope.launch {
            _showLoader.postValue(true)
            val res = mainRepository.placeOrderInDataSource(myOrder)
            when (res) {
                is ApiState.Success -> {
                    _showLoader.postValue(false)
                    if (res.data != null) {
                        _savedSuccessfully.postValue(true)
                    }
                }

                is ApiState.Failure -> {
                    val kamuDaPopup = KamuDaPopup(
                        "Error",
                        "Failed to make the order",
                        "",
                        "Close",
                        2
                    )
                    _showErrorPopup.postValue(kamuDaPopup)
                    _showLoader.postValue(false)
                    _resetList.postValue(true)
                }

                is ApiState.Loading -> {
                    _showLoader.postValue(true)
                }
            }

        }
    }

    init {
        getMenuListForMeal()
    }
}
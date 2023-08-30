package com.dilan.kamuda.customerapp.viewmodels.order

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.dilan.kamuda.customerapp.model.foodhouse.FoodMenu
import com.dilan.kamuda.customerapp.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateOrderViewModel @Inject constructor(
    private val mainRepository: MainRepository,
    application: Application
) : AndroidViewModel(application) {
    private val _menuList = MutableLiveData<List<FoodMenu>>()
    val menuList: LiveData<List<FoodMenu>>
        get() = _menuList

    private fun getMenuListForMeal(meal: String) {
        viewModelScope.launch {
            _menuList.value =
                mainRepository.getMenuListForMealFromDataSource()
        }
    }

    private fun placeOrder(){

    }

    init {
        getMenuListForMeal("breakfast")
    }
}
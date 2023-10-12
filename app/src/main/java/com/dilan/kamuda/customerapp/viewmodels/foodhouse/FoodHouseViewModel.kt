package com.dilan.kamuda.customerapp.viewmodels.foodhouse

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
class FoodHouseViewModel @Inject constructor(
    private val mainRepository: MainRepository,
    application: Application,
) : AndroidViewModel(application) {

    private val _menuList = MutableLiveData<List<FoodMenu>>()
    val menuList: LiveData<List<FoodMenu>>
        get() = _menuList

    private val _showLoader = MutableLiveData<Boolean>()
    val showLoader : LiveData<Boolean> = _showLoader

    fun getMenuListForAll() {
        _showLoader.value = true
        viewModelScope.launch {

            var list = mainRepository.getMenuListForMealFromDataSource() ?: emptyList()

            if (list.isEmpty()) {
                _menuList.postValue(list)
                _showLoader.postValue(false)
            } else {
                _menuList.postValue(list)
                _showLoader.postValue(false)
            }
        }
    }

    init {
        getMenuListForAll()
    }
}
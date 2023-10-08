package com.dilan.kamuda.customerapp.viewmodels.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.dilan.kamuda.customerapp.model.foodhouse.FoodHouse
import com.dilan.kamuda.customerapp.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val mainRepository: MainRepository,
    application: Application
) : AndroidViewModel(application) {

    private lateinit var food_house_details: FoodHouse

    private fun getFoodHouseDetails() {
        viewModelScope.launch {
            val foodhouse = mainRepository.getFoodHouseDetailsFromDataSource()
            if (foodhouse == null) {
                //TODO(Show an error message to Try again shortly)
            } else {
                food_house_details = foodhouse
            }
        }
    }

    init {
        getFoodHouseDetails()
    }
}
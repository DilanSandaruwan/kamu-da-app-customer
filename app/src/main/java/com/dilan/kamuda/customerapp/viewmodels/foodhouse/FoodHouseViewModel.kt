package com.dilan.kamuda.customerapp.viewmodels.foodhouse

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FoodHouseViewModel @Inject constructor(
    application: Application
) : AndroidViewModel(application) {
}
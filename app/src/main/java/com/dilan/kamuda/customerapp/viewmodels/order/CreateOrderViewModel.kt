package com.dilan.kamuda.customerapp.viewmodels.order

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CreateOrderViewModel @Inject constructor(
    application: Application
) : AndroidViewModel(application) {
}
package com.dilan.kamuda.customerapp.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class EmailVerificationViewModel @Inject constructor(
    application: Application
) : AndroidViewModel(application) {
}
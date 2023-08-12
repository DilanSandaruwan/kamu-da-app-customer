package com.dilan.kamuda.customerapp.views.activities

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.dilan.kamuda.customerapp.R
import com.dilan.kamuda.customerapp.databinding.ActivityCustomerRegisterBinding
import com.dilan.kamuda.customerapp.viewmodels.CustomerRegisterViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CustomerRegisterActivity : AppCompatActivity() {

    private val viewModel: CustomerRegisterViewModel by viewModels()
    private lateinit var binding: ActivityCustomerRegisterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_customer_register)
        binding.customerRegisterVM = viewModel
        binding.lifecycleOwner = this
    }
}
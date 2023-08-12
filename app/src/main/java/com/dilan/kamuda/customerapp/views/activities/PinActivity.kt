package com.dilan.kamuda.customerapp.views.activities

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.dilan.kamuda.customerapp.R
import com.dilan.kamuda.customerapp.databinding.ActivityPinBinding
import com.dilan.kamuda.customerapp.viewmodels.PinViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PinActivity : AppCompatActivity() {
    private val viewModel: PinViewModel by viewModels()
    private lateinit var binding: ActivityPinBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_pin)
        binding.pinVM = viewModel
        binding.lifecycleOwner = this
    }
}
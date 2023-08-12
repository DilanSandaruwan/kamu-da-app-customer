package com.dilan.kamuda.customerapp.views.activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.dilan.kamuda.customerapp.R
import com.dilan.kamuda.customerapp.databinding.ActivitySplashBinding
import com.dilan.kamuda.customerapp.viewmodels.SplashViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {

    lateinit var binding: ActivitySplashBinding
    val viewModel: SplashViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_splash)
        binding.splashVM = viewModel
        binding.lifecycleOwner = this

        initListeners()

    }

    private fun initListeners() {
        binding.button.setOnClickListener {
            val intent: Intent = Intent(this@SplashActivity, LoginActivity::class.java)
            startActivity(intent)
        }
    }
}
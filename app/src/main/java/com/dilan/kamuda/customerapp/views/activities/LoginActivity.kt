package com.dilan.kamuda.customerapp.views.activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.dilan.kamuda.customerapp.R
import com.dilan.kamuda.customerapp.databinding.ActivityLoginBinding
import com.dilan.kamuda.customerapp.util.KamuDaSecurePreference
import com.dilan.kamuda.customerapp.viewmodels.LoginViewModel
import com.dilan.kamuda.customerapp.views.activities.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val viewModel: LoginViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        binding.loginVM = viewModel
        binding.lifecycleOwner = this

        binding.btnLogin.setOnClickListener {
            // Storing the customer ID
            KamuDaSecurePreference().setCustomerID(this, "12");
            val intent = Intent(this@LoginActivity, MainActivity::class.java)
            startActivity(intent)
        }
    }
}
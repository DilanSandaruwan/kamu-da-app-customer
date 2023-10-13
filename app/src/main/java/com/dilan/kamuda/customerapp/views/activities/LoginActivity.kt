package com.dilan.kamuda.customerapp.views.activities

import android.content.Intent
import android.os.Bundle
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
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
            kamuDaSecurePreference.setCustomerID(this, "12");
            kamuDaSecurePreference.setUserLogged(this, true)
            val intent = Intent(this@LoginActivity, MainActivity::class.java)
            startActivity(intent)
        }

        binding.btnContinue.setOnClickListener {
            val intent = Intent(this@LoginActivity, MainActivity::class.java)
            startActivity(intent)
        }

        initialProcess()
    }

    fun initialProcess() {
        if (!kamuDaSecurePreference.isLoggedUser(this)) {
            binding.btnContinue.visibility = INVISIBLE
            binding.btnLogin.visibility = VISIBLE
            binding.textField.visibility = VISIBLE
        } else {
            binding.btnContinue.visibility = VISIBLE
            binding.btnLogin.visibility = INVISIBLE
            binding.textField.visibility = INVISIBLE
        }
    }

    companion object {
        var kamuDaSecurePreference = KamuDaSecurePreference()
    }
}
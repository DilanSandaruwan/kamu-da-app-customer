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
import com.google.android.material.textfield.TextInputEditText
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
            loginValidity()
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

    fun loginValidity() {
        if (validateMobile(binding.tvMobileNumber)) {
            if(binding.tvMobileNumber.text.toString() == "0712345678"){
                kamuDaSecurePreference.setCustomerID(this, "12");
                kamuDaSecurePreference.setUserLogged(this, true)
                val intent = Intent(this@LoginActivity, MainActivity::class.java)
                startActivity(intent)
            } else {
                binding.tvMobileNumber.error = getString(R.string.invalid_login)
                binding.tvMobileNumber.isEnabled = true
            }

        } else {

        }
    }

    private fun validateMobile(view: TextInputEditText): Boolean {
        val str = view.text.toString().trim()
        return if (str.startsWith("0")) {
            view.error = null
            view.isEnabled = false
            true
        } else {
            view.error = getString(R.string.error_contact)
            view.isEnabled = true
            false
        }
    }

    companion object {
        var kamuDaSecurePreference = KamuDaSecurePreference()
    }
}
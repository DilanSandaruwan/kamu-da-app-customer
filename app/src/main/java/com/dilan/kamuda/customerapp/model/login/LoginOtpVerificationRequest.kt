package com.dilan.kamuda.customerapp.model.login

data class LoginOtpVerificationRequest(
    val email: String,
    val code: String,
)

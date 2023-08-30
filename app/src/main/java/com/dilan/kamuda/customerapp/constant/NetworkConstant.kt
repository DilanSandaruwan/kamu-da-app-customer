package com.dilan.kamuda.customerapp.constant

import com.dilan.kamuda.customerapp.mysecret.mockapi_url

object NetworkConstant {

    const val BASE_URL = mockapi_url
    //const val BASE_URL = "http://localhost:3003/"

    // Endpoint for login
    const val ENDPOINT_LOGIN = "login"
    const val ENDPOINT_SIGNUP = "signup"
    const val ENDPOINT_2FA = "2fa"
    const val ENDPOINT_VERIFY_OTP = "verify/otp"
    const val ENDPOINT_MENU = "menu"
    const val ENDPOINT_ORDERS = "order"

}
package com.dilan.kamuda.customerapp.constant

import com.dilan.kamuda.customerapp.mysecret.route_url

object NetworkConstant {

    //const val BASE_URL = mockapi_url
    //const val BASE_URL = dongo_url
    const val BASE_URL = route_url

    // Endpoint for login
    const val ENDPOINT_LOGIN = "login"
    const val ENDPOINT_SIGNUP = "signup"
    const val ENDPOINT_2FA = "2fa"
    const val ENDPOINT_VERIFY_OTP = "verify/otp"

    // Endpoint for food-house
    const val ENDPOINT_FOODHOUSE = "foodhouse/for_customer"

    // Endpoints for menu
    const val ENDPOINT_MENU = "menu/all"

    // Endpoints for order
    const val ENDPOINT_ORDERS = "order/mylist/{id}"
    const val ENDPOINT_SAVE_ORDER = "order/save"
    const val ENDPOINT_PUT_ORDER = "order/update/status/{id}/{status}"

}

package com.dilan.kamuda.customerapp.util

import android.content.Context

class KamuDaSecurePreference {
    companion object {
        private const val PREF_NAME = "MyAppPreferences"
        private const val CUSTOMER_ID_KEY = "customer_id"
    }

    // Method to store a customer ID in SharedPreferences
    fun setCustomerID(context: Context, customerID: String) {
        val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString(CUSTOMER_ID_KEY, customerID)
        editor.apply()
    }

    // Method to retrieve the customer ID from SharedPreferences
    fun getCustomerID(context: Context): String {
        val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getString(CUSTOMER_ID_KEY, "0") ?: "0"
    }

    // Method to update the customer ID in SharedPreferences
    fun updateCustomerID(context: Context, newCustomerID: String) {
        val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString(CUSTOMER_ID_KEY, newCustomerID)
        editor.apply()
    }
}
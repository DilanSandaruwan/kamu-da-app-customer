package com.dilan.kamuda.customerapp.util

import android.content.Context

class KamuDaSecurePreference {
    companion object {
        private const val PREF_NAME = "MyAppPreferences"
        private const val CUSTOMER_ID_KEY = "customer_id"
        private const val FOOD_HOUSE_HOTLINE_KEY = "food_house_hotline"
        private const val LOAD_MY_ORDERS_KEY = "load_my_orders_id"
        private const val LOAD_MENU_FOR_ORDERS_KEY = "load_menu_for_orders_id"
        private const val IS_LOGGED_USER = "is_logged_user"
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

    // Method to store a hotline number in SharedPreferences
    fun setFoodHouseHotline(context: Context, hotline: String) {
        val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString(FOOD_HOUSE_HOTLINE_KEY, hotline)
        editor.apply()
    }

    // Method to retrieve the hotline number from SharedPreferences
    fun getFoodHouseHotline(context: Context): String {
        val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getString(FOOD_HOUSE_HOTLINE_KEY, "0717891397") ?: "0717891397"
    }

    // Method to update the customer ID in SharedPreferences
    fun updateCustomerID(context: Context, newCustomerID: String) {
        val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString(CUSTOMER_ID_KEY, newCustomerID)
        editor.apply()
    }

    // Method to save whether to reload my order details to SharedPreferences
    fun setLoadMyOrders(context: Context, doLoad: Boolean) {
        val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putBoolean(LOAD_MY_ORDERS_KEY, doLoad)
        editor.apply()
    }

    // Method to retrieve the whether to reload my order details from SharedPreferences
    fun isLoadMyOrders(context: Context): Boolean {
        val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getBoolean(LOAD_MY_ORDERS_KEY, true) ?: true
    }

    // Method to save whether to reload menu for order to SharedPreferences
    fun setLoadMenuForOrders(context: Context, doLoad: Boolean) {
        val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putBoolean(LOAD_MENU_FOR_ORDERS_KEY, doLoad)
        editor.apply()
    }

    // Method to retrieve the whether to reload menu for order from SharedPreferences
    fun isLoadMenuForOrders(context: Context): Boolean {
        val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getBoolean(LOAD_MENU_FOR_ORDERS_KEY, true) ?: true
    }

    fun setUserLogged(context: Context, isLogged: Boolean) {
        val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putBoolean(IS_LOGGED_USER, isLogged)
        editor.apply()
    }

    fun isLoggedUser(context: Context): Boolean {
        val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getBoolean(IS_LOGGED_USER, false)
    }

    fun clearSharedPrefKeys(context: Context) {
        val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.remove(LOAD_MY_ORDERS_KEY)
        editor.remove(LOAD_MENU_FOR_ORDERS_KEY)

        editor.apply()
    }

}
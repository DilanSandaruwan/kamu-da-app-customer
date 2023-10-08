package com.dilan.kamuda.customerapp.util

class Settings {

    private val kamuDaSecurePreference: KamuDaSecurePreference = TODO()

    fun setFirstName(cusFname: String) {
        TODO()
        //kamuDaSecurePreference.put(KEY_CUS_FNAME, cusFname + "")
    }

    fun getFirstName(): String {
        TODO()
        //return kamuDaSecurePreference.getString(KEY_CUS_FNAME)
        return ""

    }

    companion object {
        private const val KEY_CUS_FNAME = "cus_fname"
    }
}
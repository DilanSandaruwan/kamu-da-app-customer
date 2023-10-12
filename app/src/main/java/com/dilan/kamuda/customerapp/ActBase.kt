package com.dilan.kamuda.customerapp

import android.view.View
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint

/***
 * TODO:
 * use as a abstract class for the app features
 */
abstract class ActBase : AppCompatActivity(){
    open fun showProgress(show:Boolean){
        val progressBarHolder = findViewById<RelativeLayout>(R.id.rippleHolder)
        if (show) {
            if (!progressBarHolder.isShown) {
                progressBarHolder.visibility = View.VISIBLE
            }
        } else {
            if (progressBarHolder.isShown) {
                progressBarHolder.visibility = View.GONE
            }
        }
    }
}
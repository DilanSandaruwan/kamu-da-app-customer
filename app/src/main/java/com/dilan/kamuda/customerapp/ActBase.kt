package com.dilan.kamuda.customerapp

import android.view.View
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import com.dilan.kamuda.customerapp.model.specific.KamuDaPopup
import com.dilan.kamuda.customerapp.util.KamuDaSecurePreference
import com.dilan.kamuda.customerapp.util.ResponseHandlingDialogFragment

/***
 * use as a abstract class for the app features
 */
abstract class ActBase : AppCompatActivity() {
    open fun showProgress(show: Boolean) {
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

    open fun showErrorPopup(kamuDaPopup: KamuDaPopup): ResponseHandlingDialogFragment {
        val dialogFragment = ResponseHandlingDialogFragment.newInstance(
            title = kamuDaPopup.title,
            message = kamuDaPopup.message,
            positiveButtonText = kamuDaPopup.positiveButtonText,
            negativeButtonText = kamuDaPopup.negativeButtonText,
            type = kamuDaPopup.type,
        )
        return dialogFragment
    }

    companion object {
        var kamuDaSecurePreference = KamuDaSecurePreference()
    }
}
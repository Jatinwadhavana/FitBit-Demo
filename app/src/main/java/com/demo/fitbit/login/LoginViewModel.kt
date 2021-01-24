package com.demo.fitbit.login

import android.content.Intent
import android.view.View
import androidx.lifecycle.ViewModel
import com.demo.fitbit.fitbitactivity.FitBitActivity

/**
 * Login screen View model
 */
class LoginViewModel : ViewModel() {
    fun performFitBitLogin(view: View) {
        view.context.startActivity(Intent(view.context, FitBitActivity::class.java))
    }
}
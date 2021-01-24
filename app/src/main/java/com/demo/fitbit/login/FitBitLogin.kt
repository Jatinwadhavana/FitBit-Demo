package com.demo.fitbit.login

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.demo.fitbit.BaseActivity
import com.demo.fitbit.R
import kotlinx.android.synthetic.main.activity_fitbit_login.*
import kotlinx.android.synthetic.main.activity_fitbit_login.view.*

/**
 * Login main screen will navigate to FitBitActivity.kt
 */
class FitBitLogin : BaseActivity() {

    private lateinit var loginViewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fitbit_login)
        loginViewModel = LoginViewModel()

        toolbar.tvTitle.text=getString(R.string.ftb_login)
        setToolBar(toolbar)
        btnLogin.setOnClickListener {
            loginViewModel.performFitBitLogin(it)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home ->
                finish()
        }
        return super.onOptionsItemSelected(item)
    }
}
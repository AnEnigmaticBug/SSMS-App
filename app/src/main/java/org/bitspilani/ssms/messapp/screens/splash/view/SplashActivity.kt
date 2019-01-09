package org.bitspilani.ssms.messapp.screens.splash.view

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import org.bitspilani.ssms.messapp.R
import org.bitspilani.ssms.messapp.screens.login.view.LoginActivity
import org.bitspilani.ssms.messapp.screens.shared.view.MainActivity
import org.bitspilani.ssms.messapp.screens.splash.core.SplashViewModel
import org.bitspilani.ssms.messapp.screens.splash.core.SplashViewModelFactory
import org.bitspilani.ssms.messapp.screens.splash.view.model.UiOrder

class SplashActivity : AppCompatActivity() {

    private lateinit var viewModel: SplashViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProviders.of(this, SplashViewModelFactory())[SplashViewModel::class.java]

        setContentView(R.layout.act_splash)

        viewModel.order.observe(this, Observer {
            when(it) {
                is UiOrder.MoveToLogin   -> startActivity(Intent(this, LoginActivity::class.java))
                is UiOrder.MoveToMainApp -> startActivity(Intent(this, MainActivity::class.java))
            }
            finish()
        })
    }
}
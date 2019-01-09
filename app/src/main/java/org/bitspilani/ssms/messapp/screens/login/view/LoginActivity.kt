package org.bitspilani.ssms.messapp.screens.login.view

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.act_login_working_state.*
import kotlinx.android.synthetic.main.act_login_working_state.view.*
import org.bitspilani.ssms.messapp.R
import org.bitspilani.ssms.messapp.screens.login.core.LoginViewModel
import org.bitspilani.ssms.messapp.screens.login.core.LoginViewModelFactory
import org.bitspilani.ssms.messapp.screens.login.view.model.UiOrder
import org.bitspilani.ssms.messapp.screens.shared.view.MainActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var viewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.act_login_working_state)

        viewModel = ViewModelProviders.of(this, LoginViewModelFactory())[LoginViewModel::class.java]

        (rootPOV as ConstraintLayout).loadLayoutDescription(R.xml.ctl_states_act_login)

        rootPOV.signInBTN.setOnClickListener {
            viewModel.onLoginAction("2vwsd2g", "https://avatars0.githubusercontent.com/u/37890870?s=460&v=4")
        }

        viewModel.order.observe(this, Observer {
            when(it) {
                is UiOrder.ShowWorking   -> showWorkingState()
                is UiOrder.ShowLoading   -> showLoadingState()
                is UiOrder.MoveToMainApp -> {
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }
            }
        })
    }

    private fun showWorkingState() {
        (rootPOV as ConstraintLayout).setState(R.id.working, 0, 0)
    }

    private fun showLoadingState() {
        (rootPOV as ConstraintLayout).setState(R.id.loading, 0, 0)
    }
}
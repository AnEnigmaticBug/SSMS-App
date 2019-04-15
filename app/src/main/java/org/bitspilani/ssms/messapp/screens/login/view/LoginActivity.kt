package org.bitspilani.ssms.messapp.screens.login.view

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import kotlinx.android.synthetic.main.act_login_working_state.*
import kotlinx.android.synthetic.main.act_login_working_state.view.*
import org.bitspilani.ssms.messapp.R
import org.bitspilani.ssms.messapp.screens.login.core.LoginViewModel
import org.bitspilani.ssms.messapp.screens.login.core.LoginViewModelFactory
import org.bitspilani.ssms.messapp.screens.login.view.model.UiOrder
import org.bitspilani.ssms.messapp.screens.shared.view.MainActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var viewModel: LoginViewModel

    private val reqCode = 112

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("1049401481776-uvbpahin8kfeu7e810i88nmmlmmjcefo.apps.googleusercontent.com")
            .requestProfile()
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        setContentView(R.layout.act_login_working_state)

        viewModel = ViewModelProviders.of(this, LoginViewModelFactory())[LoginViewModel::class.java]

        (rootPOV as ConstraintLayout).loadLayoutDescription(R.xml.ctl_states_act_login)

        rootPOV.signInBTN.setOnClickListener {
            startActivityForResult(googleSignInClient.signInIntent, reqCode)
        }

        viewModel.order.observe(this, Observer {
            when(it) {
                is UiOrder.ShowWorking   -> showWorkingState()
                is UiOrder.ShowLoading   -> showLoadingState()
                is UiOrder.MoveToMainApp -> {
                    startActivity(Intent(this, MainActivity::class.java))
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                    finish()
                }
            }
        })

        viewModel.toast.observe(this, Observer {
            if(it != null) {
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == reqCode) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                viewModel.onLoginAction(account!!.idToken!!, account.photoUrl.toString())
            } catch(e: ApiException) {
                Toast.makeText(this, "Google sign in failed: ${e.statusCode}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showWorkingState() {
        googleSignInClient.signOut()
        (rootPOV as ConstraintLayout).setState(R.id.working, 0, 0)
    }

    private fun showLoadingState() {
        (rootPOV as ConstraintLayout).setState(R.id.loading, 0, 0)
    }
}
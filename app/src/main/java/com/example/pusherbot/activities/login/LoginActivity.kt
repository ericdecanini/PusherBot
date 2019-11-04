package com.example.pusherbot.activities.login

import android.app.Activity
import android.content.Intent
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.pusherbot.BR
import com.example.pusherbot.R
import com.example.pusherbot.activities.base.BaseActivity
import com.example.pusherbot.activities.lobby.LobbyActivity
import com.example.pusherbot.databinding.ActivityLoginBinding
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseUser

class LoginActivity : BaseActivity<ActivityLoginBinding, LoginViewModel>() {

    private val RC_SIGN_IN = 0

    override fun getLayoutId(): Int {
        return R.layout.activity_login
    }

    override val bindingVariable: Int
        get() = BR.loginViewModel

    override val viewModel: LoginViewModel
        get() = ViewModelProviders.of(this).get(LoginViewModel::class.java)

    override fun handleUser(user: FirebaseUser?) {
        if (user != null) {
            launchLobby()
        }
         else
            launchLoginFlow()
    }

    override fun initObservers() {
        super.initObservers()
        observeChatkitUser()
    }

    private fun observeChatkitUser() {
        this.viewModel.getChatkitUser().observe(this, Observer { user ->
            Log.d("TAG", "Observed chatkit user: $user")
            user?.let { launchLobby() }
        })
    }

    private fun launchLobby() {
        val intent = Intent(this, LobbyActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun launchLoginFlow() {
        val providers = listOf(
            AuthUI.IdpConfig.EmailBuilder().build(),
            AuthUI.IdpConfig.GoogleBuilder().build())

        startActivityForResult(AuthUI.getInstance()
            .createSignInIntentBuilder()
            .setAvailableProviders(providers)
            .build(), RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN && resultCode == Activity.RESULT_OK) {
            viewModel.refreshUser()
        }
    }

}

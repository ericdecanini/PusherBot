package com.example.pusherbot.activities.base

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import com.example.pusherbot.R
import com.example.pusherbot.activities.login.LoginActivity
import com.google.firebase.auth.FirebaseUser

abstract class BaseActivity<T: ViewDataBinding, V: BaseViewModel>: AppCompatActivity() {

    private var mViewModel: V? = null

    abstract fun getLayoutId(): Int

    abstract val bindingVariable: Int

    abstract val viewModel: V

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initDataBinding()
        observeUser()
    }

    private fun initDataBinding() {
        val binding: T = DataBindingUtil.setContentView(this, getLayoutId())
        this.mViewModel = if (mViewModel == null) viewModel else mViewModel
        binding.setVariable(bindingVariable, mViewModel)
        binding.executePendingBindings()
    }

    protected open fun initObservers() {
        // Implemented in various activities
        // Called after user is set
    }

    private fun observeUser() {
        viewModel.getUser().observe(this,
            Observer { handleUser(it) })
    }

    protected open fun handleUser(user: FirebaseUser?) {
        if (user == null) {
            launchLogin()
            finish()
        }
        else {
            Toast.makeText(this, "Logged in", Toast.LENGTH_SHORT).show()
            initObservers()
        }
    }

    private fun launchLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

}
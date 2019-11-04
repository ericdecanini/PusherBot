package com.example.pusherbot.activities.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.pusherbot.service.model.User
import com.google.firebase.auth.FirebaseUser

abstract class BaseViewModel: ViewModel() {

    val user = User()

    fun getUser(): LiveData<FirebaseUser?> {
        refreshUser()
        return user.firebaseUser
    }

    open fun refreshUser() {
        user.refreshUser()
    }

    fun signOut() {
        user.signOut()
    }

}
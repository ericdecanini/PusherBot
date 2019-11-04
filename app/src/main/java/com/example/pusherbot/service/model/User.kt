package com.example.pusherbot.service.model

import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class User {

    private val auth = FirebaseAuth.getInstance()
    val firebaseUser = MutableLiveData<FirebaseUser>()

    fun refreshUser() {
        firebaseUser.value = auth.currentUser
    }

    fun signOut() {
        auth.signOut()
        refreshUser()
    }

}
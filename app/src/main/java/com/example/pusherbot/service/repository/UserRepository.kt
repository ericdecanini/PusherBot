package com.example.pusherbot.service.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.pusherbot.service.model.CCUser
import com.google.firebase.auth.FirebaseUser
import com.pusher.chatkit.users.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserRepository {

    val chatkitApiService = ChatkitApiService()

    val chatkitUser = MutableLiveData<CCUser>()

    fun createNewUser(firebaseUser: FirebaseUser) {
        chatkitApiService.createNewUser(firebaseUser.uid, firebaseUser.displayName ?: firebaseUser.uid)
            .enqueue(object: Callback<CCUser> {
                override fun onResponse(call: Call<CCUser>, response: Response<CCUser>) {
                    chatkitUser.postValue(response.body())
                }
                override fun onFailure(call: Call<CCUser>, t: Throwable) {
                    handleError(t)
                }
            })
    }

    fun getUser(user: FirebaseUser) {
        return this.chatkitApiService.getUser(user.uid)
            .enqueue(object: Callback<CCUser?> {
                override fun onResponse(call: Call<CCUser?>, response: Response<CCUser?>) {
                    if (response.body() != null)
                        chatkitUser.postValue(response.body())
                    else
                        createNewUser(user)
                }
                override fun onFailure(call: Call<CCUser?>, t: Throwable) {
                    handleError(t)
                }
            })
    }

    private fun handleError(error: Throwable) {
        Log.e("TAG", "Error encountered while using Chatkit: ${error.message}")
        error.printStackTrace()
    }

}
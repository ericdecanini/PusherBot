package com.example.pusherbot.activities.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.pusherbot.ApiConstants
import com.example.pusherbot.activities.base.BaseViewModel
import com.example.pusherbot.service.model.CCUser
import com.example.pusherbot.service.repository.ChatkitApi
import com.example.pusherbot.service.repository.UserRepository
import com.pusher.chatkit.users.User
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class LoginViewModel: BaseViewModel() {

    private val userRepository = UserRepository()

    fun getChatkitUser(): LiveData<CCUser> {
        return userRepository.chatkitUser
    }

    fun verifyUser() {
        user.firebaseUser.value?.let {
            userRepository.getUser(it)
        }
    }


}
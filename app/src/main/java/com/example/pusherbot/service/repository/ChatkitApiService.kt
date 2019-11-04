package com.example.pusherbot.service.repository

import com.example.pusherbot.ApiConstants
import com.example.pusherbot.data.CreateUserRequestModel
import com.example.pusherbot.service.model.CCUser
import com.pusher.chatkit.users.User
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ChatkitApiService {

    val retrofit = Retrofit.Builder()
        .baseUrl(ApiConstants.CHATKIT_BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val chatkitApi = retrofit.create(ChatkitApi::class.java)

    fun createNewUser(id: String, name: String): Call<CCUser> {
        return this.chatkitApi.createNewUser(CreateUserRequestModel(id, name), "true")
    }

    fun getUser(id: String): Call<CCUser?> {
        return this.chatkitApi.getUser(id)
    }

}
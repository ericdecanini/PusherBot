package com.example.pusherbot.service.repository

import com.example.pusherbot.data.CreateUserRequestModel
import com.example.pusherbot.service.model.CCUser
import retrofit2.Call
import retrofit2.http.*

interface ChatkitApi {

        @POST("users")
        fun createNewUser(@Body request: CreateUserRequestModel, @Header("Authorization") header: String): Call<CCUser>

        @GET("users/{id}")
        fun getUser(@Path("id") id: String): Call<CCUser?>

}

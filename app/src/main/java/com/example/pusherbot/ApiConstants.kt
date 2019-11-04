package com.example.pusherbot

import kotlin.math.ceil

class ApiConstants {

    companion object {

        // CHATKIT
        val CHATKIT_INSTANCE_LOCATOR = "v1:us1:04eef84e-3d8d-4834-a379-9960d70eb4b1"
        val CHATKIT_SECRET = "7c567f15-b395-4689-af4a-3b9e6b920bc0:u5vOz8tBUWAUMI1QLPShZ3qaJElCoiuG2ccqAoZMrAg="
        val CHATKIT_BASE_URL = "https://us1.pusherplatform.io/services/chatkit_token_provider/v1/04eef84e-3d8d-4834-a379-9960d70eb4b1/"

        fun RANDOM_UID(): String {
            return "User ${ceil(Math.random() * 100)}"
        }
    }

}
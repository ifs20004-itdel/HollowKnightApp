package com.example.hollowknightapp.data.remote.response

import com.google.gson.annotations.SerializedName

data class TokenResponse(
    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("HttpStatus")
    val status: String,

    @field:SerializedName("data")
    val data: Token
)

data class Token (
    @field:SerializedName("name")
    val name: String,

    @field:SerializedName("token")
    val token: String
    )
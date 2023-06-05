package com.example.hollowknightapp.data.remote.response

import com.google.gson.annotations.SerializedName


data class RegisterResponse(
    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("HttpStatus")
    val status: String,

    @field:SerializedName("data")
    val data: Token
)

data class User(
    @field:SerializedName("name")
    val name: String,

    @field:SerializedName("email")
    val email: String,

    @field:SerializedName("password")
    val password:String,

    @field:SerializedName("roles")
    val roles: String
)
package com.example.hollowknightapp.utils

interface AuthenticationCallback {
    fun onError(isLogin:Boolean?, message: String?)
}
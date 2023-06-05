package com.example.hollowknightapp.data.retrofit

import com.example.hollowknightapp.data.remote.response.HeroResponse
import com.example.hollowknightapp.data.remote.response.RegisterResponse
import com.example.hollowknightapp.data.remote.response.TokenResponse
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @GET("api/v1/heroes")
    fun getHero(
        @Header("Authorization") token: String
    ): Call<List<HeroResponse>>

    @GET("{heroId}")
    fun getSingleHero(
        @Header("Authorization") token: String,
        @Path("heroId") heroId: String
    ): Call<HeroResponse>

    @POST("auth/register")
    fun register(
        @Body registerUser: RequestBody
    ):Call<RegisterResponse>

    @POST("auth/login")
    fun login(
        @Body loginUser: RequestBody
    ):Call<TokenResponse>
}
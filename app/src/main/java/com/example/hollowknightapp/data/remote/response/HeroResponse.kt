package com.example.hollowknightapp.data.remote.response

import com.google.gson.annotations.SerializedName

data class HeroResponse(
       @field:SerializedName("id")
        val id: Int,

       @field:SerializedName("heroId")
       val heroID: String,

       @field:SerializedName("name")
       val name: String,

       @field:SerializedName("photoUrl")
       val photoUrl: String,

       @field:SerializedName("gender")
       val gender: String
)

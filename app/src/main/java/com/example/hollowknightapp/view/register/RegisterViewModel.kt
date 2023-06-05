package com.example.hollowknightapp.view.register

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.hollowknightapp.data.remote.response.RegisterResponse
import com.example.hollowknightapp.data.retrofit.ApiConfig
import com.example.hollowknightapp.model.UserPreference
import com.example.hollowknightapp.utils.AuthenticationCallback
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterViewModel(private val pref: UserPreference): ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading : LiveData<Boolean> = _isLoading

    fun validateRegister(name:String, email:String, password: String, roles: String, stateCallback: AuthenticationCallback){
        _isLoading.value = true
        val apiService = ApiConfig.getApiService()
        val json = """
            { 
                "name" : "$name",
                "email": "$email",
                "password": "$password",
                "roles": "$roles"
            }
        """.trimIndent()
        val body = json.toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
        val register = apiService.register(body)
        register.enqueue(object : Callback<RegisterResponse> {
            override fun onResponse(
                call: Call<RegisterResponse>,
                response: Response<RegisterResponse>
            ) {
                _isLoading.value = false
                if(response.isSuccessful){
                    val responseBody = response.body()
                    if(responseBody!=null && responseBody.status == "OK"){
                        stateCallback.onError(false, responseBody.message)
                    }
                }else{
                    stateCallback.onError(true, "There's something wrong")
                }
            }
            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(ContentValues.TAG,"OnFailure: ${t.message.toString()}")
                stateCallback.onError(true, t.message.toString())
            }
        })
    }
}
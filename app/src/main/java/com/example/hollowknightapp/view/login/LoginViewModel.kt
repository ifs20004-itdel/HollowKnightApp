package com.example.hollowknightapp.view.login

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.*
import com.example.hollowknightapp.data.remote.response.TokenResponse
import com.example.hollowknightapp.data.retrofit.ApiConfig
import com.example.hollowknightapp.model.UserModel
import com.example.hollowknightapp.model.UserPreference
import com.example.hollowknightapp.utils.AuthenticationCallback
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel(private val pref: UserPreference):ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading :LiveData<Boolean> = _isLoading


    fun getUser():LiveData<UserModel>{
        return pref.getUser().asLiveData()
    }

    fun login(){
        viewModelScope.launch {
            pref.login()
        }
    }

    fun saveUser(user: UserModel){
        viewModelScope.launch {
            pref.saveUser(user)
        }
    }

    fun validateLogin(username:String, password: String, stateCallback: AuthenticationCallback){
        _isLoading.value = true
        val apiService = ApiConfig.getApiService()
        val json = """
            {
            "username": "$username",
            "password": "$password"
            }
        """.trimIndent()
        val body = json.toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
        val login = apiService.login(body)
        login.enqueue(object : Callback<TokenResponse> {
            override fun onResponse(call: Call<TokenResponse>, response: Response<TokenResponse>) {
                _isLoading.value = false
                val responseBody = response.body()
                if(response.isSuccessful){
                    if(responseBody !=null && responseBody.status == "OK"){
                        login()
                        val logged = responseBody.data
                        Log.e("tes", logged.toString())
                        saveUser(
                            UserModel(
                                logged.name,
                                logged.token,
                                true
                            )
                        )
                    }
                }else{
                    stateCallback.onError(true, "Username or password are wrong")
                }
            }
            override fun onFailure(call: Call<TokenResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(ContentValues.TAG,"OnFailure: ${t.message.toString()}")
                stateCallback.onError(true, t.message.toString())
            }
        })
    }
}
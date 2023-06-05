package com.example.hollowknightapp.view.main

import android.util.Log
import androidx.lifecycle.*
import com.example.hollowknightapp.data.remote.response.HeroResponse
import com.example.hollowknightapp.data.retrofit.ApiConfig
import com.example.hollowknightapp.model.UserModel
import com.example.hollowknightapp.model.UserPreference
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainFragmentViewModel(private val pref: UserPreference): ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _user = MutableLiveData<List<HeroResponse>>()
    val user: LiveData<List<HeroResponse>> = _user

    fun getToken():LiveData<UserModel>{
        return pref.getUser().asLiveData()
    }

    fun getHero(token: String){
        _isLoading.value = true
        val client = ApiConfig.getApiService().getHero("Bearer $token")
        client.enqueue(object : Callback<List<HeroResponse>> {
            override fun onResponse(
                call: Call<List<HeroResponse>>,
                response: Response<List<HeroResponse>>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        _user.value = response.body()
                    } else {
                        Log.e(TAG, "onFailure: ${response.message()}")
                    }
                }
            }
            override fun onFailure(call: Call<List<HeroResponse>>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message}")
            }
        }
        )
    }

    companion object{
        private const val TAG = ".MainFragment"
    }
}
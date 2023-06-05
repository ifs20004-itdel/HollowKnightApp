package com.example.hollowknightapp.view.bottomNavigation

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.hollowknightapp.model.UserModel
import com.example.hollowknightapp.model.UserPreference
import kotlinx.coroutines.launch

class BottomNavigationViewModel(private val pref: UserPreference): ViewModel() {

    fun getUser(): LiveData<UserModel> {
        return pref.getUser().asLiveData()
    }

    fun login(){
        viewModelScope.launch {
            pref.login()
        }
    }

    fun logout(){
        viewModelScope.launch {
            pref.logout()
        }
    }
}
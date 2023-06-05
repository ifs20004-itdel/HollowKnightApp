package com.example.hollowknightapp.view

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.hollowknightapp.model.UserPreference
import com.example.hollowknightapp.view.bottomNavigation.BottomNavigationActivity
import com.example.hollowknightapp.view.bottomNavigation.BottomNavigationViewModel
import com.example.hollowknightapp.view.login.LoginViewModel
import com.example.hollowknightapp.view.main.MainFragmentViewModel
import com.example.hollowknightapp.view.register.RegisterViewModel

class ViewModelFactory(private val pref: UserPreference, private val context: Context): ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when{
            modelClass.isAssignableFrom(LoginViewModel::class.java)->{
                LoginViewModel(pref) as T
            }
            modelClass.isAssignableFrom(BottomNavigationViewModel::class.java)->{
                BottomNavigationViewModel(pref) as T
            }
            modelClass.isAssignableFrom(RegisterViewModel::class.java)->{
                RegisterViewModel(pref) as T
            }
            modelClass.isAssignableFrom(MainFragmentViewModel::class.java)->{
                MainFragmentViewModel(pref) as T
            }
//            modelClass.isAssignableFrom(MapViewModel::class.java)->{
//                MapViewModel(pref) as T
//            }
            else-> throw IllegalArgumentException("Unknown ViewModel class: "+modelClass.name)
        }
    }
}
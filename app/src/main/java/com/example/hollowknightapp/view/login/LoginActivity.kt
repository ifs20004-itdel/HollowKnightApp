package com.example.hollowknightapp.view.login

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.hollowknightapp.R
import com.example.hollowknightapp.databinding.ActivityLoginBinding
import com.example.hollowknightapp.model.UserPreference
import com.example.hollowknightapp.utils.AuthenticationCallback
import com.example.hollowknightapp.view.ViewModelFactory
import com.example.hollowknightapp.view.bottomNavigation.BottomNavigationActivity
import com.example.hollowknightapp.view.generateLink
import com.example.hollowknightapp.view.register.RegisterActivity


private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class LoginActivity : AppCompatActivity(), AuthenticationCallback {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var loginViewModel: LoginViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupActionBar()
        setupViewModel()
        setupRegisterLink()
        setupAction()
    }

    private fun setupViewModel(){
        loginViewModel =  ViewModelProvider(
            this,
            ViewModelFactory(UserPreference.getInstance(dataStore), this)
        )[LoginViewModel::class.java]

        loginViewModel.getUser().observe(this){
                user ->
            if(user.state){
                val intent = Intent(this@LoginActivity, BottomNavigationActivity::class.java )
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            }
        }
        loginViewModel.isLoading.observe(this){
                isLoading->
            showLoading(isLoading)
        }
    }

    private fun setupRegisterLink() {
        val registerLabel = binding.registerLabel
        registerLabel.generateLink(
            Pair(resources.getString(R.string.register) , View.OnClickListener {
                val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            })
        )
    }

    private fun setupActionBar() {
        @Suppress("DEPRECATION")
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        }else{
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }

    private fun setupAction(){
        binding.btnLogin.setOnClickListener{
            val email = binding.loginUsername.text.toString()
            val password = binding.loginPassword.text.toString()
            when{
                email.isEmpty()->{
                    binding.textInputLayoutEmail.error = resources.getString(R.string.empty_error)
                }
                password.isEmpty()->{
                    binding.textInputLayoutPassword.error = resources.getString(R.string.empty_error)
                }
                else->{
                    binding.textInputLayoutEmail.error = ""
                    binding.textInputLayoutPassword.error = ""
                    loginViewModel.validateLogin(email, password, this)
                }
            }
        }
    }

    private fun showLoading(isLoading: Boolean){
        if(isLoading){
            binding.progressBar.visibility  = View.VISIBLE
            binding.progressBar.progress = 0
            binding.progressBar.max = 100
        }else{
            binding.progressBar.visibility = View.GONE
        }
    }

    override fun onError(isLogin: Boolean?, message: String?) {
        Toast.makeText(this@LoginActivity, message.toString(), Toast.LENGTH_SHORT).show()
    }
}
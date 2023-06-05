package com.example.hollowknightapp.view.register

import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.example.hollowknightapp.R
import com.example.hollowknightapp.databinding.ActivityRegisterBinding
import com.example.hollowknightapp.model.UserPreference
import com.example.hollowknightapp.utils.AuthenticationCallback
import com.example.hollowknightapp.utils.isValidEmail
import com.example.hollowknightapp.view.ViewModelFactory
import com.example.hollowknightapp.view.generateLink
import com.example.hollowknightapp.view.login.LoginActivity

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class RegisterActivity : AppCompatActivity(), AuthenticationCallback {
    private lateinit var binding : ActivityRegisterBinding
    private lateinit var registerViewModel: RegisterViewModel
    private var roles = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupLoginLink()
        setupActionBar()
        setupViewModel()
        setupAction()
    }

    private fun setupAction() {
        binding.btnRegister.setOnClickListener {
            val name = binding.registerName.text.toString()
            val email = binding.registerEmail.text.toString()
            val password = binding.registerPassword.text.toString()
            when{
                name.isEmpty()->{
                    binding.textInputLayoutNameRegister.error = resources.getString(R.string.empty_error)
                }
                email.isEmpty()->{
                    binding.textInputLayoutEmailRegister.error = resources.getString(R.string.empty_error)
                }
                password.isEmpty()->{
                    binding.textInputLayoutPasswordRegister.error = resources.getString(R.string.empty_error)
                }
                !email.isValidEmail()->{
                    binding.textInputLayoutEmailRegister.error = resources.getString(R.string.email_validation)
                }
                roles.isEmpty()->{
                    Toast.makeText(this, "Radio Button can't be blank", Toast.LENGTH_SHORT).show()
                }
                else->{
                    binding.textInputLayoutNameRegister.error = ""
                    binding.textInputLayoutPasswordRegister.error = ""
                    binding.textInputLayoutPasswordRegister.error = ""
                    registerViewModel.validateRegister(name, email, password, roles,this)
                }
            }
        }
    }

    private fun setupViewModel() {
        registerViewModel =  ViewModelProvider(
            this,
            ViewModelFactory(UserPreference.getInstance(dataStore), this)
        )[RegisterViewModel::class.java]

        registerViewModel.isLoading.observe(this){
            showLoading(it)
        }
    }

    private fun setupLoginLink() {
            val loginLabel = binding.loginLabel
            loginLabel.generateLink(
                Pair(resources.getString(R.string.login) , View.OnClickListener {
                    val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                })
            )
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

    override fun onError(isLogin: Boolean?, message: String?) {
        if(isLogin == true){
            Toast.makeText(this@RegisterActivity, message, Toast.LENGTH_SHORT).show()
        }else{
            val builder = AlertDialog.Builder(this@RegisterActivity)
            builder
                .setTitle(R.string.success)
                .setMessage(R.string.register_success)
                .setPositiveButton(R.string.login){
                        _,_->
                    val intent = Intent(
                        this@RegisterActivity,
                        LoginActivity::class.java
                    )
                    intent.flags =
                        Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                }
            builder.create()
            builder.show()
        }
    }

    fun onRadioButtonClicked(view: View) {
        if(view is RadioButton){
            val checked = view.isChecked
            when(view.id){
                R.id.radio_admin->{
                    if(checked){
                        roles = resources.getString(R.string.radio_admin)
                    }
                }
                R.id.radio_user->{
                    if(checked){
                        roles = resources.getString(R.string.radio_user)
                    }
                }
            }
        }
    }
}
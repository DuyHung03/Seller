package com.example.seller.view.activity

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.seller.R
import com.example.seller.databinding.ActivityLoginBinding
import com.example.seller.utils.InputValidation
import com.example.seller.utils.ValidationTextWatcher
import com.example.seller.viewmodel.DataViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val dataViewModel by viewModels<DataViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onResume() {
        super.onResume()
        val validationTextWatcher =
            ValidationTextWatcher(listOf(binding.emailInputLayout, binding.passwordInputLayout))
        binding.edtEmail.addTextChangedListener(validationTextWatcher)
        binding.edtPassword.addTextChangedListener(validationTextWatcher)

        binding.loginButton.setOnClickListener {
            if (inputValidation())
                dataViewModel.signInEmail(
                    binding.edtEmail.text.toString().trim(),
                    binding.edtPassword.text.toString().trim()
                ) { user ->
                    user?.let {
                        toHomeScreen()
                    }
                }
        }
    }

    private fun toHomeScreen() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(
            intent,
            ActivityOptions.makeCustomAnimation(
                this,
                R.anim.slide_in_right,
                R.anim.slide_out_left
            )
                .toBundle()
        )
        this.finish()
    }

    private fun inputValidation(): Boolean {
        val emailText = binding.edtEmail.text.toString().trim()
        val passwordText = binding.edtPassword.text.toString().trim()
        val inputValidation = InputValidation()
        Log.d("TAG", "handleLoginEmail: $emailText")
        return (inputValidation.isValidEmail(emailText) { error ->
            binding.emailInputLayout.error = error
        } && inputValidation.isValidPassword(passwordText) { error ->
            binding.passwordInputLayout.error = error
        })
    }

}
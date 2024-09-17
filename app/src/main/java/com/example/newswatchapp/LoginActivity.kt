package com.example.newswatchapp

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.MotionEvent
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.example.newswatchapp.R

class LoginActivity : AppCompatActivity() {

    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnSignIn: Button
    private lateinit var btnRegister: Button
    private lateinit var tvForgotPassword: TextView
    private lateinit var ivGoogle: ImageView
    private lateinit var ivFacebook: ImageView
    private lateinit var ivTwitter: ImageView
    private lateinit var ivApple: ImageView
    private lateinit var auth: FirebaseAuth
    private var isPasswordVisible = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()

        // Initialize UI components
        etEmail = findViewById(R.id.et_email)
        etPassword = findViewById(R.id.et_password)
        btnSignIn = findViewById(R.id.btn_sign_in)
        btnRegister = findViewById(R.id.btn_register)
        tvForgotPassword = findViewById(R.id.tv_forgot_password)
        ivGoogle = findViewById(R.id.iv_google)
        ivFacebook = findViewById(R.id.iv_facebook)
        ivTwitter = findViewById(R.id.iv_twitter)
        ivApple = findViewById(R.id.iv_apple)

        // Set up Sign In button click listener
        btnSignIn.setOnClickListener {
            handleSignIn()
        }

        // Set up Register click listener
        btnRegister.setOnClickListener {
            handleRegister()
        }

        // Set up Forgot Password listener
        tvForgotPassword.setOnClickListener {
            handleForgotPassword()
        }

        // Set up social media login listeners
        ivGoogle.setOnClickListener {
            handleGoogleSignIn()
        }

        ivFacebook.setOnClickListener {
            handleFacebookSignIn()
        }

        ivTwitter.setOnClickListener {
            handleTwitterSignIn()
        }

        ivApple.setOnClickListener {
            handleAppleSignIn()
        }

        // Handle password visibility toggle
        etPassword.setOnTouchListener { _, event ->
            val drawableEnd = 2 // Right side drawable
            if (event.action == MotionEvent.ACTION_UP) {
                if (event.rawX >= (etPassword.right - etPassword.compoundDrawables[drawableEnd].bounds.width())) {
                    isPasswordVisible = !isPasswordVisible
                    togglePasswordVisibility(isPasswordVisible)
                    return@setOnTouchListener true
                }
            }
            false
        }
    }

    private fun handleSignIn() {
        val email = etEmail.text.toString().trim()
        val password = etPassword.text.toString().trim()

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show()
            return
        }

        // Firebase Authentication to log in the user
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, redirect to the main activity or home
                    Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, ArticlePostActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    // If sign-in fails, display a message to the user
                    Toast.makeText(this, "Authentication Failed: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                }
            }
    }

    private fun handleRegister() {
        // Navigate to registration activity
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
    }

    private fun handleForgotPassword() {
        // Handle forgot password functionality
        val email = etEmail.text.toString().trim()
        if (email.isEmpty()) {
            Toast.makeText(this, "Please enter your email", Toast.LENGTH_SHORT).show()
            return
        }

        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Password reset email sent", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Failed to send reset email: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                }
            }
    }

    private fun handleGoogleSignIn() {
        // Handle Google Sign In logic
        Toast.makeText(this, "Google Sign-In not implemented", Toast.LENGTH_SHORT).show()
    }

    private fun handleFacebookSignIn() {
        // Handle Facebook Sign In logic
        Toast.makeText(this, "Facebook Sign-In not implemented", Toast.LENGTH_SHORT).show()
    }

    private fun handleTwitterSignIn() {
        // Handle Twitter Sign In logic
        Toast.makeText(this, "Twitter Sign-In not implemented", Toast.LENGTH_SHORT).show()
    }

    private fun handleAppleSignIn() {
        // Handle Apple Sign In logic
        Toast.makeText(this, "Apple Sign-In not implemented", Toast.LENGTH_SHORT).show()
    }

    private fun togglePasswordVisibility(isVisible: Boolean) {
        if (isVisible) {
            etPassword.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            etPassword.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_visibility, 0) // Show icon
        } else {
            etPassword.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            etPassword.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_visibility, 0) // Hide icon
        }
        etPassword.setSelection(etPassword.text.length) // Move cursor to end
    }
}

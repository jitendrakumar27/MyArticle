package com.example.newswatchapp



import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class VerifyOtpActivity : AppCompatActivity() {

    private lateinit var ivAppLogo: ImageView
    private lateinit var tvEnterEmail: TextView
    private lateinit var etEmail: EditText
    private lateinit var btnGetOtp: Button
    private lateinit var etVerificationCode: EditText
    private lateinit var btnVerifyOtp: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_verify_otp) // Ensure this matches your XML layout file name

        // Initialize UI components
        ivAppLogo = findViewById(R.id.iv_app_logo)
        tvEnterEmail = findViewById(R.id.tv_enter_email)
        etEmail = findViewById(R.id.et_email)
        btnGetOtp = findViewById(R.id.btn_get_otp)
        etVerificationCode = findViewById(R.id.et_verification_code)
        btnVerifyOtp = findViewById(R.id.btn_verify_otp)

        // Set up button click listeners
        btnGetOtp.setOnClickListener {
            handleGetOtp()
        }

        btnVerifyOtp.setOnClickListener {
            handleVerifyOtp()
        }
    }

    private fun handleGetOtp() {
        val email = etEmail.text.toString()

        // Basic validation
        if (email.isBlank()) {
            Toast.makeText(this, "Please enter your email", Toast.LENGTH_SHORT).show()
            return
        }

        // Handle OTP request logic here
        // For example, send a request to the server to send OTP to the email

        // Show a success message
        Toast.makeText(this, "OTP sent to $email", Toast.LENGTH_SHORT).show()

        // Optionally, you can update UI or state if needed
        etVerificationCode.isEnabled = true
        btnVerifyOtp.isEnabled = true
    }

    private fun handleVerifyOtp() {
        val verificationCode = etVerificationCode.text.toString()

        // Basic validation
        if (verificationCode.isBlank()) {
            Toast.makeText(this, "Please enter the verification code", Toast.LENGTH_SHORT).show()
            return
        }

        // Handle OTP verification logic here
        // For example, send a request to the server to verify the OTP

        // Show a success message
        Toast.makeText(this, "OTP verified successfully", Toast.LENGTH_SHORT).show()

        // Optionally, navigate to the next screen or activity
        // Intent intent = new Intent(this, NextActivity::class.java)
        // startActivity(intent)
    }
}

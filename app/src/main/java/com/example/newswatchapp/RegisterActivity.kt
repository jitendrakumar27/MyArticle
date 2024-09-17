package com.example.newswatchapp

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore

class RegisterActivity : AppCompatActivity() {

    private lateinit var etUsername: EditText
    private lateinit var etEmail: EditText
    private lateinit var etPhoneNumber: EditText
    private lateinit var etPassword: EditText
    private lateinit var rgRole: RadioGroup
    private lateinit var rbMediaReporter: RadioButton
    private lateinit var rbVisitor: RadioButton
    private lateinit var btnSignUp: Button
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        // Initialize Firebase Auth and Firestore
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        // Initialize UI components
        etUsername = findViewById(R.id.et_username_register)
        etEmail = findViewById(R.id.et_email_register)
        etPhoneNumber = findViewById(R.id.et_phone_number)
        etPassword = findViewById(R.id.et_password_register)
        rgRole = findViewById(R.id.rg_role)
        rbMediaReporter = findViewById(R.id.rb_media_reporter)
        rbVisitor = findViewById(R.id.rb_visitor)
        btnSignUp = findViewById(R.id.btn_sign_up)

        // Set up Sign Up button click listener
        btnSignUp.setOnClickListener {
            handleSignUp()
        }
    }

    private fun handleSignUp() {
        val username = etUsername.text.toString().trim()
        val email = etEmail.text.toString().trim()
        val phoneNumber = etPhoneNumber.text.toString().trim()
        val password = etPassword.text.toString().trim()
        val selectedRole = when {
            rbMediaReporter.isChecked -> "Media Reporter"
            rbVisitor.isChecked -> "Visitor"
            else -> ""
        }

        if (email.isEmpty() || password.isEmpty() || username.isEmpty() || phoneNumber.isEmpty() || selectedRole.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            return
        }

        // Create new user using Firebase Authentication
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign up success, save user info to Firestore
                    val firebaseUser: FirebaseUser? = auth.currentUser
                    firebaseUser?.let {
                        saveUserInfo(it.uid, username, email, phoneNumber, selectedRole)
                    }
                } else {
                    // If sign up fails, display a message to the user.
                    Toast.makeText(this, "Registration Failed: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                }
            }
    }

    private fun saveUserInfo(uid: String, username: String, email: String, phone: String, role: String) {
        // Create user data map
        val userData = hashMapOf(
            "username" to username,
            "email" to email,
            "phone" to phone,
            "role" to role
        )

        // Save to Firestore under 'users' collection
        db.collection("users").document(uid).set(userData)
            .addOnSuccessListener {
                // Data saved successfully, navigate back to login
                Toast.makeText(this, "Registration Successful", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
            .addOnFailureListener { e ->
                // Error occurred
                Toast.makeText(this, "Error saving user data: ${e.message}", Toast.LENGTH_LONG).show()
            }
    }
}

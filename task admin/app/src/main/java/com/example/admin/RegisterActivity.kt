package com.example.admin

import android.content.Intent
import android.os.Bundle
import android.system.Os.uname
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.admin.Model.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class RegisterActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().reference

        val mail = findViewById<EditText>(R.id.mail)
        val pwd = findViewById<EditText>(R.id.pwd)
        val confirm_pwd = findViewById<EditText>(R.id.confirm_pwd)
        val submit_btn = findViewById<Button>(R.id.submit_btn)
        val already = findViewById<TextView>(R.id.alreadyHave)

        submit_btn.setOnClickListener {
            val email = mail.text.toString().trim()
            val password = pwd.text.toString().trim()
            val confirmPassword = confirm_pwd.text.toString().trim()

            if (email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(this, "Fill all details", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Registered Success", Toast.LENGTH_SHORT).show()
                registerUser(email = email, username = "", password = password)
            }
        }

        already.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

    }

    private fun registerUser(username: String, email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(this, "Account Created Successfully", Toast.LENGTH_SHORT).show()
                saveUserData()
                val intent = Intent(this, HomeActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "Account Creation Failed", Toast.LENGTH_SHORT).show()
                Log.d("Account", "createAccount: Failure", task.exception)
            }
        }
    }
    private fun saveUserData() {
        val email = findViewById<EditText>(R.id.mail).text.toString().trim()
        val password = findViewById<EditText>(R.id.pwd).text.toString().trim()
        val userId = FirebaseAuth.getInstance().currentUser!!.uid
        val user = UserModel(email, password)
        database.child("admin").child(userId).setValue(user)
    }
}
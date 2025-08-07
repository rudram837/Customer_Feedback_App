package com.example.task

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().reference

        val uname_login = findViewById<EditText>(R.id.uname_login)
        val pwd_login = findViewById<EditText>(R.id.pwd_login)
        val logini_btn = findViewById<Button>(R.id.login_btn)
        val dontHavelo = findViewById<TextView>(R.id.dontHavelo)

        logini_btn.setOnClickListener {
            val username = uname_login.text.toString().trim()
            val password = pwd_login.text.toString().trim()

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Fill details", Toast.LENGTH_SHORT).show()
            } else {
                login(username, password)
                Toast.makeText(this, "Login Successfully", Toast.LENGTH_SHORT).show()
            }
        }

        dontHavelo.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun login(username: String, password: String) {
        auth.signInWithEmailAndPassword(username, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    Toast.makeText(this, "Login Successfully", Toast.LENGTH_SHORT).show()
                    updateUi(user)
                } else {
                    Toast.makeText(
                        this,
                        "You don't have an account\nCreate Account",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    private fun updateUi(user: FirebaseUser?) {
        if (user != null) {
            val intent = Intent(this, FeedbackActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if (currentUser != null) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}

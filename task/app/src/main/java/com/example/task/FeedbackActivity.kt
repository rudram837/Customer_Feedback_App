package com.example.task

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.FirebaseDatabase

class FeedbackActivity : AppCompatActivity() {

    private lateinit var writefeedback: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feedback)

        val addfeedback = findViewById<Button>(R.id.addfeedback)
        val editfeedback = findViewById<Button>(R.id.editfeedback)
        val submit = findViewById<Button>(R.id.submit)
        writefeedback = findViewById(R.id.writefeedback)

        addfeedback.setOnClickListener {
            writefeedback.isEnabled = true
            submit.isEnabled = true
            writefeedback.requestFocus()
        }

        editfeedback.setOnClickListener {
            val intent = Intent(this, HistoryFeedbackActivity::class.java)
            startActivity(intent)
        }

        submit.setOnClickListener {
            val feedbackText = writefeedback.text.toString().trim()

            if (feedbackText.isEmpty()) {
                Toast.makeText(this, "Fill the feedback", Toast.LENGTH_SHORT).show()
            } else {
                savefeedback(feedbackText)
                Toast.makeText(this, "Feedback submitted successfully", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun savefeedback(feedbackText: String) {
        val user = com.google.firebase.auth.FirebaseAuth.getInstance().currentUser

        if (user == null) {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show()
            return
        }

        val uid = user.uid
        val database = FirebaseDatabase.getInstance()
        val feedbackRef = database.getReference("feedbacks").child(uid)

        val feedbackId = feedbackRef.push().key

        val feedbackData = mapOf(
            "id" to feedbackId,
            "text" to feedbackText,
            "timestamp" to System.currentTimeMillis()
        )

        if (feedbackId != null) {
            feedbackRef.child(feedbackId).setValue(feedbackData)
                .addOnSuccessListener {
                    Toast.makeText(this, "Feedback submitted successfully", Toast.LENGTH_SHORT).show()
                    writefeedback.apply {
                        setText("")
                        isEnabled = false
                    }
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Failed: ${it.message}", Toast.LENGTH_SHORT).show()
                }
        } else {
            Toast.makeText(this, "Feedback ID is null", Toast.LENGTH_SHORT).show()
        }
    }


}

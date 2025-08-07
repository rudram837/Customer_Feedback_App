package com.example.admin

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.FirebaseDatabase

class EditActivity : AppCompatActivity() {

    private lateinit var editFeedbackBox: EditText
    private lateinit var updateButton: Button

    private var userId: String? = null
    private var feedbackId: String? = null
    private var feedbackText: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)

        editFeedbackBox = findViewById(R.id.edit_feedback_box)
        updateButton = findViewById(R.id.update_feedback_btn)

        // Get data from intent
        userId = intent.getStringExtra("userId")
        feedbackId = intent.getStringExtra("feedbackId")
        feedbackText = intent.getStringExtra("feedbackText")

        // Set existing text
        editFeedbackBox.setText(feedbackText)

        updateButton.setOnClickListener {
            val updatedText = editFeedbackBox.text.toString().trim()

            if (updatedText.isEmpty()) {
                Toast.makeText(this, "Feedback cannot be empty", Toast.LENGTH_SHORT).show()
            } else {
                updateFeedbackInFirebase(updatedText)
            }
        }
    }

    private fun updateFeedbackInFirebase(updatedText: String) {
        if (userId != null && feedbackId != null) {
            val ref = FirebaseDatabase.getInstance().getReference("feedbacks")
                .child(userId!!)
                .child(feedbackId!!)

            val updateData = mapOf(
                "text" to updatedText,
                "timestamp" to System.currentTimeMillis()
            )

            ref.updateChildren(updateData).addOnSuccessListener {
                Toast.makeText(this, "Feedback updated successfully", Toast.LENGTH_SHORT).show()
                finish()
            }.addOnFailureListener {
                Toast.makeText(this, "Failed to update: ${it.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}

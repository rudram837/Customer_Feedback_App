package com.example.task

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class EditFeedbackActivity : AppCompatActivity() {

    private lateinit var editFeedbackBox: EditText
    private lateinit var updateButton: Button
    private lateinit var goBackEdit: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_feedbak)

        editFeedbackBox = findViewById(R.id.edit_feedback_box)
        updateButton = findViewById(R.id.update_feedback_btn)
        goBackEdit = findViewById(R.id.go_back_home)

        goBackEdit.setOnClickListener {
            finish()
        }

        val oldFeedback = intent.getStringExtra("feedbackText")
        val feedbackKey = intent.getStringExtra("feedbackKey")

        editFeedbackBox.setText(oldFeedback)

        updateButton.setOnClickListener {
            val newText = editFeedbackBox.text.toString().trim()
            val uid = FirebaseAuth.getInstance().currentUser?.uid

            if (uid != null && feedbackKey != null) {
                val ref = FirebaseDatabase.getInstance()
                    .getReference("feedbacks")
                    .child(uid)
                    .child(feedbackKey)

                val updatedMap = mapOf(
                    "text" to newText,
                    "timestamp" to System.currentTimeMillis()
                )

                ref.updateChildren(updatedMap).addOnSuccessListener {
                    Toast.makeText(this, "Updated successfully", Toast.LENGTH_SHORT).show()
                    finish()
                }.addOnFailureListener {
                    Toast.makeText(this, "Failed to update", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}

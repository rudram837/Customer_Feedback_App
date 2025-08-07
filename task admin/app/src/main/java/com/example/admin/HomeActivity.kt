package com.example.admin

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.admin.Adapter.FeedbackAdapter
import com.example.admin.Model.Feedback
import com.google.firebase.database.*

class HomeActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var feedbackList: ArrayList<Pair<String, Feedback>>
    private lateinit var adapter: FeedbackAdapter
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        recyclerView = findViewById(R.id.all_feedback)
        recyclerView.layoutManager = LinearLayoutManager(this)
        feedbackList = ArrayList()
        database = FirebaseDatabase.getInstance().getReference("feedbacks")

        database.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                feedbackList.clear()

                for (userSnap in snapshot.children) {
                    val userId = userSnap.key ?: continue

                    for (feedbackSnap in userSnap.children) {
                        val feedbackId = feedbackSnap.key ?: continue
                        val feedback = feedbackSnap.getValue(Feedback::class.java)

                        if (feedback != null) {
                            feedbackList.add(Pair("$userId/$feedbackId", feedback))
                        }
                    }
                }

                adapter = FeedbackAdapter(feedbackList) { userId, feedbackId ->
                    deleteFeedback(userId, feedbackId)
                }

                recyclerView.adapter = adapter
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@HomeActivity, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun deleteFeedback(userId: String, feedbackId: String) {
        val ref = FirebaseDatabase.getInstance().getReference("feedbacks/$userId/$feedbackId")
        ref.removeValue().addOnSuccessListener {
            Toast.makeText(this, "Feedback deleted", Toast.LENGTH_SHORT).show()
            recreate() // Refresh UI
        }.addOnFailureListener {
            Toast.makeText(this, "Failed to delete", Toast.LENGTH_SHORT).show()
        }
    }
}

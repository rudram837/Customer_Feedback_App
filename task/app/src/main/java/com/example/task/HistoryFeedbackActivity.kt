package com.example.task

import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.task.Adapter.FeedbackAdapter
import com.example.task.Model.Feedback
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class HistoryFeedbackActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: FeedbackAdapter
    private lateinit var feedbackList: ArrayList<Feedback>
    private lateinit var feedbackKeys: ArrayList<String>
    private lateinit var goBack: ImageView

    private lateinit var auth: FirebaseAuth
    private lateinit var reference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history_feedback)

        goBack = findViewById(R.id.go_back_home)
        goBack.setOnClickListener {
            finish()
        }

        recyclerView = findViewById(R.id.history_feedback_recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        feedbackList = ArrayList()
        feedbackKeys = ArrayList()

        auth = FirebaseAuth.getInstance()
        val userId = auth.currentUser?.uid

        if (userId != null) {
            reference = FirebaseDatabase.getInstance().getReference("feedbacks").child(userId)

            reference.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    feedbackList.clear()
                    feedbackKeys.clear()

                    for (feedbackSnap in snapshot.children) {
                        val feedback = feedbackSnap.getValue(Feedback::class.java)
                        val key = feedbackSnap.key
                        if (feedback != null && key != null) {
                            feedbackList.add(feedback)
                            feedbackKeys.add(key)
                        }
                    }

                    adapter = FeedbackAdapter(this@HistoryFeedbackActivity, feedbackList, feedbackKeys)
                    recyclerView.adapter = adapter
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(this@HistoryFeedbackActivity, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }
}

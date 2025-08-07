package com.example.task.Adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.task.EditFeedbackActivity
import com.example.task.Model.Feedback
import com.example.task.R
import java.text.SimpleDateFormat
import java.util.*

class FeedbackAdapter(
    private val context: Context,
    private val feedbackList: List<Feedback>,
    private val feedbackKeys: List<String>
) : RecyclerView.Adapter<FeedbackAdapter.FeedbackViewHolder>() {

    class FeedbackViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val contentTextView: TextView = itemView.findViewById(R.id.feedback_content)
        val timeTextView: TextView = itemView.findViewById(R.id.feedback_time)
        val editIcon: ImageView = itemView.findViewById(R.id.editicon)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedbackViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_feedback, parent, false)
        return FeedbackViewHolder(view)
    }

    override fun onBindViewHolder(holder: FeedbackViewHolder, position: Int) {
        val feedback = feedbackList[position]
        val key = feedbackKeys[position]

        holder.contentTextView.text = feedback.text
        val sdf = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault())
        val timeString = sdf.format(Date(feedback.timestamp))
        holder.timeTextView.text = timeString

        holder.editIcon.setOnClickListener {
            val intent = Intent(context, EditFeedbackActivity::class.java)
            intent.putExtra("feedbackText", feedback.text)
            intent.putExtra("feedbackKey", key)
            context.startActivity(intent)
        }
    }

    override fun getItemCount() = feedbackList.size
}

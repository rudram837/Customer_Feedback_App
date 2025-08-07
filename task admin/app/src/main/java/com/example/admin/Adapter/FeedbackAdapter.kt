package com.example.admin.Adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.admin.EditActivity
import com.example.admin.Model.Feedback
import com.example.admin.R
import java.text.SimpleDateFormat
import java.util.*

class FeedbackAdapter(
    private val feedbackList: List<Pair<String, Feedback>>,
    private val onDeleteClick: (userId: String, feedbackId: String) -> Unit
) : RecyclerView.Adapter<FeedbackAdapter.FeedbackViewHolder>() {

    class FeedbackViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val feedbackText: TextView = itemView.findViewById(R.id.feedback_content)
        val timestampText: TextView = itemView.findViewById(R.id.feedback_time)
        val trashIcon: ImageView = itemView.findViewById(R.id.trash)
        val editIcon: ImageView = itemView.findViewById(R.id.editicon)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedbackViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_feedback, parent, false)
        return FeedbackViewHolder(view)
    }

    override fun onBindViewHolder(holder: FeedbackViewHolder, position: Int) {
        val (fullKey, feedback) = feedbackList[position]
        holder.feedbackText.text = feedback.text
        holder.timestampText.text = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault()).format(Date(feedback.timestamp))

        val split = fullKey.split("/") // "userId/feedbackId"
        val userId = split[0]
        val feedbackId = split[1]

        // Delete action
        holder.trashIcon.setOnClickListener {
            onDeleteClick(userId, feedbackId)
        }

        // Edit action - Navigate to EditFeedbackActivity
        holder.editIcon.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, EditActivity::class.java).apply {
                putExtra("userId", userId)
                putExtra("feedbackId", feedbackId)
                putExtra("feedbackText", feedback.text)
            }
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = feedbackList.size
}

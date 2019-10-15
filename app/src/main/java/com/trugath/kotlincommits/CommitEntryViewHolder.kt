package com.trugath.kotlincommits

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

import com.bumptech.glide.Glide
import com.trugath.kotlincommits.db.CommitEntry
import org.joda.time.format.DateTimeFormat


class CommitEntryViewHolder(itemView: View):
    RecyclerView.ViewHolder(itemView) {

    private val timeFormatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss").withZoneUTC()

    private val title: TextView? = itemView.findViewById(R.id.title)
    private val username: TextView? = itemView.findViewById(R.id.username)
    private val avatar: ImageView? = itemView.findViewById(R.id.avatar)
    private val dateTime: TextView? = itemView.findViewById(R.id.dateTime)

    fun bind(commitEntry: CommitEntry?) {
        if (commitEntry != null) {
            title?.text = commitEntry.title
            username?.text = commitEntry.authorName
            avatar?.let { avatar ->
                commitEntry.authorAvatar?.let { url ->
                    Glide.with(itemView).load(url).into(avatar)
                }
            }
            dateTime?.text = commitEntry.timeStamp.toString(timeFormatter)
        }
    }

    fun recycle() {
        avatar?.let { Glide.with(itemView).clear(it) }
    }
}
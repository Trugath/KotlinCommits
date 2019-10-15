package com.trugath.kotlincommits

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.trugath.kotlincommits.db.CommitEntry

class CommitEntryAdapter (
    context: Context
) : RecyclerView.Adapter<CommitEntryViewHolder>() {

    private var data: MutableList<CommitEntry>? = null
    private val layoutInflater: LayoutInflater

    init {
        this.data = ArrayList()
        this.layoutInflater =
            context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommitEntryViewHolder {
        val itemView = layoutInflater.inflate(R.layout.commit_row, parent, false)
        return CommitEntryViewHolder(
            itemView)
    }

    override fun onBindViewHolder(holder: CommitEntryViewHolder, position: Int) {
        holder.bind(data?.get(position))
    }

    override fun onViewRecycled(holder: CommitEntryViewHolder) {
        holder.recycle()
    }

    override fun getItemCount(): Int {
        return data?.size ?: 0
    }

    fun setData(newData: MutableList<CommitEntry>?) {
        val oldData = data ?: mutableListOf()
        val historyEntryDiffCallback = CommitEntryDiffCallback(oldData, newData ?: listOf())
        val diffResult = DiffUtil.calculateDiff(historyEntryDiffCallback, false)

        oldData.clear()
        newData?.let { oldData.addAll(it) }
        data = oldData
        diffResult.dispatchUpdatesTo(this)
    }
}
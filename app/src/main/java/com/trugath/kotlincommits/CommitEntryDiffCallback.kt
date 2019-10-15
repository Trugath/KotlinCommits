package com.trugath.kotlincommits

import androidx.recyclerview.widget.DiffUtil
import com.trugath.kotlincommits.db.CommitEntry

class CommitEntryDiffCallback(
    private val oldHistoryEntries: List<CommitEntry>,
    private val newHistoryEntries: List<CommitEntry>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int {
        return oldHistoryEntries.size
    }

    override fun getNewListSize(): Int {
        return newHistoryEntries.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldHistoryEntries[oldItemPosition].sha === newHistoryEntries[newItemPosition].sha
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldHistoryEntries[oldItemPosition].sha === newHistoryEntries[newItemPosition].sha
    }
}

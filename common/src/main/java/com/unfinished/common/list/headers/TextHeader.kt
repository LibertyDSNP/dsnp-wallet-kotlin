package com.unfinished.common.list.headers

import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import com.unfinished.common.R
import com.unfinished.common.list.GroupedListHolder
import com.unfinished.common.utils.inflateChild

class TextHeader(val content: String) {

    companion object {

        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<TextHeader>() {

            override fun areItemsTheSame(oldItem: TextHeader, newItem: TextHeader): Boolean {
                return oldItem.content == newItem.content
            }

            override fun areContentsTheSame(oldItem: TextHeader, newItem: TextHeader): Boolean {
                return true
            }
        }
    }
}

class TextHeaderHolder(parentView: ViewGroup) : GroupedListHolder(parentView.inflateChild(R.layout.item_text_header)) {

    fun bind(item: TextHeader) {
        (containerView as? TextView)?.text = item.content
    }
}

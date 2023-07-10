package com.unfinished.common.list

import android.view.View
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

abstract class BaseListAdapter<T, VH : BaseViewHolder>(diffCallback: DiffUtil.ItemCallback<T>) : ListAdapter<T, VH>(diffCallback) {

    override fun onViewRecycled(holder: VH) {
        holder.unbind()
    }
}

abstract class BaseViewHolder(val containerView: View) : RecyclerView.ViewHolder(containerView) {

    abstract fun unbind()
}

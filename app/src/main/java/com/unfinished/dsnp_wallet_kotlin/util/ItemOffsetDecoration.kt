package com.unfinished.dsnp_wallet_kotlin.util

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.annotation.DimenRes
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration

class ItemOffsetDecoration(private val mItemOffset: Int) : ItemDecoration() {
    constructor(
        @NonNull context: Context,
        @DimenRes itemOffsetId: Int
    ) : this(context.getResources().getDimensionPixelSize(itemOffsetId)) {
    }

    override fun getItemOffsets(
        outRect: Rect, view: View, parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        outRect.set(mItemOffset, mItemOffset, mItemOffset, mItemOffset)
    }
}
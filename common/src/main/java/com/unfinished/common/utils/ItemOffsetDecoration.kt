package com.unfinished.common.utils

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.annotation.DimenRes
import androidx.recyclerview.widget.RecyclerView

class ItemOffsetDecoration(
    context: Context,
    @DimenRes itemVerticalOffset: Int,
    @DimenRes itemHorizontalOffset: Int
) : RecyclerView.ItemDecoration() {

    private val _itemVerticalOffset: Int
    private var _itemHorizontalOffset: Int = 0

    init {
        _itemVerticalOffset = context.resources.getDimensionPixelSize(itemVerticalOffset)
        _itemHorizontalOffset = context.resources.getDimensionPixelSize(itemHorizontalOffset)
    }

    /**
     * Applies padding to all sides of the [Rect], which is the container for the view
     */
    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView,state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)
        outRect.set(_itemHorizontalOffset, _itemVerticalOffset, _itemHorizontalOffset, _itemVerticalOffset)
    }
}
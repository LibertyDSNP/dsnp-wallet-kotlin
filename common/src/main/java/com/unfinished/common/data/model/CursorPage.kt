package com.unfinished.common.data.model

data class CursorPage<T>(
    val nextCursor: String?,
    val items: List<T>
) : List<T> by items

package com.unfinished.runtime.util.resource

import android.content.Context

class StringResourceManagerImpl(
    private val context: Context
) : ResourceManager {

    override fun getString(res: Int): String {
        return context.getString(res)
    }
}

package com.unfinished.data.util.resource

import android.content.Context
import javax.inject.Singleton
import kotlin.time.ExperimentalTime

class ResourceManagerImpl(
    private val context: Context
) : ResourceManager {

    override fun getString(res: Int): String {
        return context.getString(res)
    }
}

package com.unfinished.common.di

import android.content.Context

object FeatureUtils {

    fun getCommonApi(context: Context): CommonApi {
        return context.applicationContext as CommonApi
    }
}

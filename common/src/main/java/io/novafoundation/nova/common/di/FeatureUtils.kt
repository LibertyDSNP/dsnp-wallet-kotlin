package io.novafoundation.nova.common.di

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.assisted.AssistedFactory

object FeatureUtils {

    fun getCommonApi(context: Context): CommonApi {
        return context.applicationContext as CommonApi
    }
}

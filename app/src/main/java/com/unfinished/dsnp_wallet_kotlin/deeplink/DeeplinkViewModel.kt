package com.unfinished.dsnp_wallet_kotlin.deeplink

import android.content.Intent
import com.unfinished.common.base.BaseViewModel
import com.unfinished.dsnp_wallet_kotlin.BuildConfig
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class DeeplinkViewModel @Inject constructor(

) : BaseViewModel() {

    private val _deeplinkStateFlow = MutableStateFlow<Deeplink>(Deeplink.None)
    val deeplinkStateFlow = _deeplinkStateFlow.asStateFlow()

    fun setDeeplink(intent: Intent?) {
        _deeplinkStateFlow.value = intent?.data?.toString()?.let {
            if (it.contains(BuildConfig.WEB_URL)) Deeplink.Valid(
                url = it,
                onLinkUsed = { _deeplinkStateFlow.value = Deeplink.None }
            ) else Deeplink.None
        } ?: Deeplink.None
    }
}
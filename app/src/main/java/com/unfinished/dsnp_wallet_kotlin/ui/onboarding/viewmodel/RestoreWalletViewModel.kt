package com.unfinished.dsnp_wallet_kotlin.ui.onboarding.viewmodel

import com.unfinished.dsnp_wallet_kotlin.ui.onboarding.uimodel.CreateIdentityUiModel
import com.unfinished.dsnp_wallet_kotlin.ui.onboarding.uimodel.RestoreWalletUiModel
import com.unfinished.uikit.UiState
import com.unfinished.uikit.toDataLoaded
import dagger.hilt.android.lifecycle.HiltViewModel
import io.novafoundation.nova.common.base.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class RestoreWalletViewModel @Inject constructor(

): BaseViewModel() {
    private val _uiStateFLow =
        MutableStateFlow<UiState<RestoreWalletUiModel>>(RestoreWalletUiModel().toDataLoaded())
    val uiStateFLow = _uiStateFLow.asStateFlow()
}
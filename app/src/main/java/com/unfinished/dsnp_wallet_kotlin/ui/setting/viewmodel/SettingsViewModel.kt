package com.unfinished.dsnp_wallet_kotlin.ui.setting.viewmodel

import com.unfinished.dsnp_wallet_kotlin.ui.setting.uimodel.Setting
import com.unfinished.dsnp_wallet_kotlin.ui.setting.uimodel.SettingsUiModel
import com.unfinished.uikit.UiState
import com.unfinished.uikit.toDataLoaded
import dagger.hilt.android.lifecycle.HiltViewModel
import io.novafoundation.nova.common.base.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(

) : BaseViewModel() {

    private val _uiStateFLow =
        MutableStateFlow<UiState<SettingsUiModel>>(
            SettingsUiModel(
                settings = listOf(
                    Setting.Security, Setting.FaceId(), Setting.Password
                )
            ).toDataLoaded()
        )

    val uiStateFLow = _uiStateFLow.asStateFlow()
}
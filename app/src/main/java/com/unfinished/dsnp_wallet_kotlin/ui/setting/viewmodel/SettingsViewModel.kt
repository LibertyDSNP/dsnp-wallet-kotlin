package com.unfinished.dsnp_wallet_kotlin.ui.setting.viewmodel

import com.unfinished.dsnp_wallet_kotlin.ui.setting.uimodel.Setting
import com.unfinished.dsnp_wallet_kotlin.ui.setting.uimodel.SettingsUiModel
import com.unfinished.uikit.UiState
import com.unfinished.uikit.toDataLoaded
import dagger.hilt.android.lifecycle.HiltViewModel
import com.unfinished.common.base.BaseViewModel
import com.unfinished.data.storage.Preferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val preferences: Preferences
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

    private val _logoutDialogStateFlow = MutableStateFlow<Logout>(Logout.Hide)
    val logoutDialogStateFlow = _logoutDialogStateFlow.asStateFlow()

    fun showLogoutDialog() {
        _logoutDialogStateFlow.value = Logout.Show
    }

    fun hideLogoutDialog() {
        _logoutDialogStateFlow.value = Logout.Hide
    }

    fun logout() {
        /**
         * TODO
         */
        preferences.clear()
    }

    sealed class Logout : UiState<Unit> {
        object Show : Logout()
        object Hide : Logout()
    }

}
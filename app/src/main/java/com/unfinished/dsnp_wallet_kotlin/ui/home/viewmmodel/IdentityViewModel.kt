package com.unfinished.dsnp_wallet_kotlin.ui.home.viewmmodel

import com.unfinished.dsnp_wallet_kotlin.R
import com.unfinished.dsnp_wallet_kotlin.ui.home.uimodel.IdentityTask
import com.unfinished.dsnp_wallet_kotlin.ui.home.uimodel.IdentityUiModel
import com.unfinished.uikit.UiState
import com.unfinished.uikit.toDataLoaded
import dagger.hilt.android.lifecycle.HiltViewModel
import com.unfinished.common.base.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class IdentityViewModel @Inject constructor(

) : BaseViewModel() {

    private val _uiStateFLow =
        MutableStateFlow<UiState<IdentityUiModel>>(UiState.Loading())
    val uiStateFLow = _uiStateFLow.asStateFlow()

    init {
        /**
         * TODO: make calls to fetch info for ui state
         */

        _uiStateFLow.value = IdentityUiModel(
            iconUrl = null,
            username = "neverendingwinter.23",
            identityTasks = listOf(
                IdentityTask(
                    title = R.string.set_avatar
                ),
                IdentityTask(
                    title = R.string.backup_seed_phrase
                ),
                IdentityTask(
                    title = R.string.choose_a_handle,
                    isComplete = true
                )
            )
        ).toDataLoaded()
    }
}
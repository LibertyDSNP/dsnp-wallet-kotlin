package com.unfinished.dsnp_wallet_kotlin.ui.recovery.viewmodel

import com.unfinished.dsnp_wallet_kotlin.ui.recovery.uimodel.RecoveryPhraseUiModel
import com.unfinished.dsnp_wallet_kotlin.ui.recovery.uimodel.SeedKey
import com.unfinished.dsnp_wallet_kotlin.ui.recovery.uimodel.SeedKeyState
import com.unfinished.uikit.UiState
import com.unfinished.uikit.toDataLoaded
import dagger.hilt.android.lifecycle.HiltViewModel
import io.novafoundation.nova.common.base.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class RecoveryPhraseViewModel @Inject constructor(

) : BaseViewModel() {

    private val _uiStateFLow = MutableStateFlow<UiState<RecoveryPhraseUiModel>>(UiState.Loading())
    val uiStateFLow = _uiStateFLow.asStateFlow()

    init {
        _uiStateFLow.value = RecoveryPhraseUiModel(
            seedKeys = listOf(
                SeedKey(prefix = "01", key = "Satisy"),
                SeedKey(prefix = "02", key = "Spike"),
                SeedKey(prefix = "03", key = "Lake"),
                SeedKey(prefix = "04", key = "Cupcake"),
                SeedKey(prefix = "05", key = "Bag"),
                SeedKey(prefix = "06", key = "Turmoil"),
                SeedKey(prefix = "07", key = "Sunny"),
                SeedKey(prefix = "08", key = "Rainbow"),
                SeedKey(prefix = "09", key = "Truck"),
                SeedKey(prefix = "10", key = "Train"),
                SeedKey(prefix = "11", key = "Running"),
                SeedKey(prefix = "12", key = "Spin"),
            )
        ).toDataLoaded()
    }

    fun shuffleSeedKeys() {
        (_uiStateFLow.value as? UiState.DataLoaded)?.data?.let {
            _uiStateFLow.value = it.copy(
                currentRandomSeedKeys = it.seedKeys.shuffled(),
                seedKeyState = SeedKeyState.Init
            ).toDataLoaded()
        }
    }

    fun addSeedKeyToGuess(seedKey: SeedKey) {
        (_uiStateFLow.value as? UiState.DataLoaded)?.data?.let {
            _uiStateFLow.value = it.copy(
                currentSeedGuesses = it.currentSeedGuesses.toMutableList()
                    .apply { add(seedKey) },
                seedKeyState = SeedKeyState.Init
            ).toDataLoaded()
        }
    }

    fun removeSeedKeyFromGuess(seedKey: SeedKey) {
        (_uiStateFLow.value as? UiState.DataLoaded)?.data?.let {
            _uiStateFLow.value = it.copy(
                currentSeedGuesses = it.currentSeedGuesses.toMutableList()
                    .apply { remove(seedKey) },
                seedKeyState = SeedKeyState.Init
            ).toDataLoaded()
        }
    }

    fun verifySeedKeys() {
        (_uiStateFLow.value as? UiState.DataLoaded)?.data?.let {
            val invalidSeedKeys = it.seedKeys != it.currentSeedGuesses

            _uiStateFLow.value = it.copy(
                seedKeyState = if (invalidSeedKeys) SeedKeyState.NotValid else SeedKeyState.Finish,
                currentSeedGuesses = if (invalidSeedKeys) emptyList() else it.currentSeedGuesses
            ).toDataLoaded()
        }
    }
}
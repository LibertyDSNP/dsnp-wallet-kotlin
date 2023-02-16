package io.novafoundation.nova.splash.presentation

import androidx.lifecycle.viewModelScope
import com.unfinished.dsnp_wallet_kotlin.ui.splash.SplashRouter
import com.unfinished.feature_account.domain.interfaces.AccountRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import io.novafoundation.nova.common.base.BaseViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val router: SplashRouter,
    private val repository: AccountRepository
) : BaseViewModel() {

    init {
        openInitialDestination()
    }

    private fun openInitialDestination() {
        viewModelScope.launch {
            if (repository.isAccountSelected()) {
                if (repository.isCodeSet()) {
                    router.openInitialCheckPincode()
                } else {
                    router.openCreatePincode()
                }
            } else {
                router.openAddFirstAccount()
            }
        }
    }
}

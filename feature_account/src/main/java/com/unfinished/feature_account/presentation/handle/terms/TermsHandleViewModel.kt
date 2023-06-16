package com.unfinished.feature_account.presentation.handle.terms

import com.unfinished.feature_account.presentation.AccountRouter
import dagger.hilt.android.lifecycle.HiltViewModel

import io.novafoundation.nova.common.base.BaseViewModel
import javax.inject.Inject

@HiltViewModel
class TermsHandleViewModel @Inject constructor(
    private val router: AccountRouter,
) : BaseViewModel() {

    fun openTabScreen(skip: Boolean, identitySuccess: Boolean){
        router.openTabScreen(skip,identitySuccess)
    }

}

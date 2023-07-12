package com.unfinished.account.presentation.handle.terms

import com.unfinished.account.presentation.AccountRouter
import dagger.hilt.android.lifecycle.HiltViewModel

import com.unfinished.common.base.BaseViewModel
import javax.inject.Inject

@HiltViewModel
class TermsHandleViewModel @Inject constructor(
    private val router: AccountRouter,
) : BaseViewModel() {

    fun openTabScreen(skip: Boolean, identitySuccess: Boolean){
        router.openTabScreen(skip,identitySuccess)
    }

}

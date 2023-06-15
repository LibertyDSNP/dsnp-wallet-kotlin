package com.unfinished.feature_account.presentation.handle.terms

import com.unfinished.feature_account.presentation.AccountRouter
import com.unfinished.feature_account.presentation.model.account.add.AddAccountPayload
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel

import io.novafoundation.nova.common.base.BaseViewModel
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class TermsHandleViewModel @Inject constructor(
    private val router: AccountRouter,
) : BaseViewModel() {

    fun openHomeScreen(skip: Boolean, identitySuccess: Boolean){
        router.openHomeScreenFromHandle(skip,identitySuccess)
    }

}

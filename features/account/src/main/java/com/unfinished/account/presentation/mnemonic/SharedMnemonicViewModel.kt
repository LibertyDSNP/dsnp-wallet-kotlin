package com.unfinished.account.presentation.mnemonic

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.unfinished.account.presentation.AccountRouter
import com.unfinished.account.presentation.mnemonic.confirm.CongratulationDialogButton
import com.unfinished.common.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import com.unfinished.common.utils.Event
import javax.inject.Inject

@HiltViewModel
class SharedMnemonicViewModel @Inject constructor(
    private val router: AccountRouter,
) : BaseViewModel() {

    private val _warningDialogAction = MutableLiveData<Event<WarningDialogButton>>()
    val warningDialogAction: LiveData<Event<WarningDialogButton>> = _warningDialogAction

    private val _congDialogAction = MutableLiveData<Event<CongratulationDialogButton>>()
    val congDialogAction: LiveData<Event<CongratulationDialogButton>> = _congDialogAction

    fun invokeWarningButtonAction(action: WarningDialogButton){
        _warningDialogAction.value = Event(action)
    }

    fun invokeCongButtonAction(action: CongratulationDialogButton){
        _congDialogAction.value = Event(action)
    }
}
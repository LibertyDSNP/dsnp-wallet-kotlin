package com.unfinished.dsnp_wallet_kotlin.ui.onboarding

import androidx.lifecycle.MutableLiveData
import com.unfinished.feature_account.presentation.mixin.importType.ImportTypeChooserMixin
import com.unfinished.feature_account.presentation.model.account.add.AddAccountPayload
import com.unfinished.feature_account.presentation.model.account.add.ImportAccountPayload
import dagger.hilt.android.lifecycle.HiltViewModel
import io.novafoundation.nova.common.base.BaseViewModel
import io.novafoundation.nova.common.data.network.AppLinksProvider
import io.novafoundation.nova.common.mixin.api.Browserable
import io.novafoundation.nova.common.utils.Event
import javax.inject.Inject

@HiltViewModel
class LandingViewModel @Inject constructor(
    private val router: OnboardingRouter,
    private val appLinksProvider: AppLinksProvider,
    private val importTypeChooserMixin: ImportTypeChooserMixin.Presentation,
) : BaseViewModel(),
    ImportTypeChooserMixin by importTypeChooserMixin,
    Browserable {

    lateinit var addAccountPayload: AddAccountPayload
    var shouldShowBack: Boolean = false

    var walletName = "TEST"

    override val openBrowserEvent = MutableLiveData<Event<String>>()

    fun setBundleArguments(shouldShowBack: Boolean, addAccountPayload: AddAccountPayload){
        this.shouldShowBack = shouldShowBack
        this.addAccountPayload = addAccountPayload
    }

    fun openCreateHandleScreen(){
        router.openCreateHandleScreen()
    }
    fun openLookupScreen(){
        router.openLookupScreen()
    }

    fun createAccountClicked() {
        when (addAccountPayload) {
            is AddAccountPayload.MetaAccount -> router.openCreateAccount(accountName = walletName,addAccountPayload)
            is AddAccountPayload.ChainAccount -> router.openMnemonicScreen(accountName = walletName, addAccountPayload)
        }
    }

    fun importAccountClicked() {
        val payload = ImportTypeChooserMixin.Payload(
            onChosen = { router.openImportAccountScreen(ImportAccountPayload(it, addAccountPayload)) }
        )

        importTypeChooserMixin.showChooser(payload)
    }

    fun termsClicked() {
        openBrowserEvent.value = Event(appLinksProvider.termsUrl)
    }

    fun privacyClicked() {
        openBrowserEvent.value = Event(appLinksProvider.privacyUrl)
    }

    fun backClicked() {
        router.back()
    }

}

package com.unfinished.feature_account.di

import android.content.Context
import com.unfinished.feature_account.domain.account.add.AddAccountInteractor
import com.unfinished.feature_account.domain.interactor.AdvancedEncryptionInteractor
import com.unfinished.feature_account.presentation.AdvancedEncryptionCommunicator
import dagger.Module
import dagger.Provides
import io.novafoundation.nova.common.mixin.MixinFactory
import io.novafoundation.nova.common.resources.ClipboardManager
import com.unfinished.feature_account.presentation.importing.FileReader
import com.unfinished.feature_account.presentation.importing.source.ImportSourceFactory
import com.unfinished.feature_account.presentation.mixin.AccountNameChooserFactory
import com.unfinished.feature_account.presentation.mixin.AccountNameChooserMixin
import com.unfinished.feature_account.presentation.model.account.add.ImportAccountPayload
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object ImportModule {

    @Provides
    fun provideImportSourceFactory(
        addAccountInteractor: AddAccountInteractor,
        clipboardManager: ClipboardManager,
        advancedEncryptionRequester: AdvancedEncryptionCommunicator,
        fileReader: FileReader,
        advancedEncryptionInteractor: AdvancedEncryptionInteractor,
    ) = ImportSourceFactory(
        addAccountInteractor = addAccountInteractor,
        clipboardManager = clipboardManager,
        advancedEncryptionInteractor = advancedEncryptionInteractor,
        advancedEncryptionRequester = advancedEncryptionRequester,
        fileReader = fileReader
    )

    @Provides
    fun provideNameChooserMixinFactory(
        payload: ImportAccountPayload,
    ): MixinFactory<AccountNameChooserMixin.Presentation> {
        return AccountNameChooserFactory(payload.addAccountPayload)
    }

    @Provides
    fun provideFileReader(context: Context) = FileReader(context)

}

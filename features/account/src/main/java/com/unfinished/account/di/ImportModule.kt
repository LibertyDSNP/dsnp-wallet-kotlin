package com.unfinished.account.di

import android.content.Context
import com.unfinished.account.domain.account.add.AddAccountInteractor
import com.unfinished.account.domain.interactor.AdvancedEncryptionInteractor
import com.unfinished.account.presentation.AdvancedEncryptionCommunicator
import dagger.Module
import dagger.Provides
import com.unfinished.common.mixin.MixinFactory
import com.unfinished.common.resources.ClipboardManager
import com.unfinished.account.presentation.importing.FileReader
import com.unfinished.account.presentation.importing.source.ImportSourceFactory
import com.unfinished.account.presentation.mixin.AccountNameChooserFactory
import com.unfinished.account.presentation.mixin.AccountNameChooserMixin
import com.unfinished.account.presentation.model.account.add.ImportAccountPayload
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

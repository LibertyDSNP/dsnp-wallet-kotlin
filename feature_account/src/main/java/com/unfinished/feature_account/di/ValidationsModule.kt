package com.unfinished.feature_account.di

import com.unfinished.feature_account.domain.account.export.json.validations.ExportJsonPasswordValidation
import com.unfinished.feature_account.domain.account.export.json.validations.ExportJsonPasswordValidationSystem
import com.unfinished.feature_account.domain.account.export.json.validations.PasswordMatchConfirmationValidation
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.scopes.FragmentScoped
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet
import io.novafoundation.nova.common.validation.from
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ValidationsModule {

    @Provides
    @Singleton
    @IntoSet
    fun passwordMatchConfirmationValidation(): ExportJsonPasswordValidation = PasswordMatchConfirmationValidation()

    @Provides
    @Singleton
    fun provideValidationSystem(
        validations: Set<@JvmSuppressWildcards ExportJsonPasswordValidation>
    ) = ExportJsonPasswordValidationSystem.from(validations)
}

package com.unfinished.account.di

import com.unfinished.account.domain.account.export.json.validations.ExportJsonPasswordValidation
import com.unfinished.account.domain.account.export.json.validations.ExportJsonPasswordValidationSystem
import com.unfinished.account.domain.account.export.json.validations.PasswordMatchConfirmationValidation
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet
import com.unfinished.common.validation.from
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ValidationsModule {

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

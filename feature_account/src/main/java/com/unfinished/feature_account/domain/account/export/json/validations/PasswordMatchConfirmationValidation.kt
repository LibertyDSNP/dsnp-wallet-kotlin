package com.unfinished.feature_account.domain.account.export.json.validations

import io.novafoundation.nova.common.validation.ValidationStatus
import io.novafoundation.nova.common.validation.validOrError

class PasswordMatchConfirmationValidation : ExportJsonPasswordValidation {

    override suspend fun validate(value: ExportJsonPasswordValidationPayload): ValidationStatus<ExportJsonPasswordValidationFailure> {
        return validOrError(value.password == value.passwordConfirmation) {
            ExportJsonPasswordValidationFailure.PASSWORDS_DO_NOT_MATCH
        }
    }
}

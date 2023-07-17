package com.unfinished.account.domain.account.export.json.validations

import com.unfinished.common.validation.ValidationStatus
import com.unfinished.common.validation.validOrError

class PasswordMatchConfirmationValidation : ExportJsonPasswordValidation {

    override suspend fun validate(value: ExportJsonPasswordValidationPayload): ValidationStatus<ExportJsonPasswordValidationFailure> {
        return validOrError(value.password == value.passwordConfirmation) {
            ExportJsonPasswordValidationFailure.PASSWORDS_DO_NOT_MATCH
        }
    }
}

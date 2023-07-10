package com.unfinished.feature_account.domain.account.export.json.validations

import com.unfinished.common.base.TitleAndMessage
import com.unfinished.common.resources.ResourceManager
import com.unfinished.common.R

enum class ExportJsonPasswordValidationFailure {
    PASSWORDS_DO_NOT_MATCH
}

fun mapExportJsonPasswordValidationFailureToUi(
    resourceManager: ResourceManager,
    failure: ExportJsonPasswordValidationFailure,
): TitleAndMessage {
    return when (failure) {
        ExportJsonPasswordValidationFailure.PASSWORDS_DO_NOT_MATCH -> resourceManager.getString(R.string.common_error_general_title) to
            resourceManager.getString(R.string.export_json_password_match_error)
    }
}

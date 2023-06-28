package com.unfinished.dsnp_wallet_kotlin.ui.setting.uimodel

import androidx.annotation.StringRes
import com.unfinished.dsnp_wallet_kotlin.R

data class SettingsUiModel(
    val settings: List<Setting>,
    val showSnackbar: Boolean = false
)

sealed class Setting(
    @StringRes val title: Int,
    @StringRes val desc: Int
) {
    object Security : Setting(
        title = R.string.security,
        desc = R.string.security_desc
    )

    class FaceId(val isToggled: Boolean = false) : Setting(
        title = R.string.face_id,
        desc = R.string.face_id_desc
    )

    object Password : Setting(
        title = R.string.password,
        desc = R.string.password_desc
    )
}
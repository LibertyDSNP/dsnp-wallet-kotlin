package com.unfinished.dsnp_wallet_kotlin.ui.home.uimodel

import androidx.annotation.StringRes

data class IdentityUiModel(
    val iconUrl: String?,
    val username: String,
    val identityTasks: List<IdentityTask>
) {

    val totalCount: Int
        get() = identityTasks.size

    val currentCount: Int
        get() = identityTasks.count { it.isComplete }
}

data class IdentityTask(
    @StringRes val title: Int,
    val isComplete: Boolean = false
)
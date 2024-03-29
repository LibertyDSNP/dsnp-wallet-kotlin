package com.unfinished.common.utils.permissions

import com.unfinished.common.mixin.actionAwaitable.ActionAwaitableMixin

typealias Permission = String

interface PermissionsAsker {
    enum class PermissionDeniedAction {
        RETRY, BACK
    }

    enum class PermissionDeniedLevel {
        CAN_ASK_AGAIN, REQUIRE_SETTINGS_CHANGE
    }

    val showPermissionsDenied: ActionAwaitableMixin<PermissionDeniedLevel, PermissionDeniedAction>

    interface Presentation : PermissionsAsker {
        suspend fun requirePermissionsOrExit(vararg permissions: Permission): Boolean
    }
}

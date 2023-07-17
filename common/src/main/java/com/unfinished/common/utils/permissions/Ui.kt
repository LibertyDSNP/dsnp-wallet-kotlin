package com.unfinished.common.utils.permissions

import com.unfinished.common.R
import com.unfinished.common.base.BaseFragment
import com.unfinished.common.utils.permissions.PermissionsAsker.PermissionDeniedAction
import com.unfinished.common.utils.permissions.PermissionsAsker.PermissionDeniedLevel.CAN_ASK_AGAIN
import com.unfinished.common.view.dialog.warningDialog

fun BaseFragment<*>.setupPermissionAsker(
    component: PermissionsAsker,
) {
    component.showPermissionsDenied.awaitableActionLiveData.observeEvent {
        val level = it.payload

        warningDialog(
            context = requireContext(),
            onConfirm = { it.onSuccess(PermissionDeniedAction.RETRY) },
            onCancel = { it.onSuccess(PermissionDeniedAction.BACK) },
            confirmTextRes = if (level == CAN_ASK_AGAIN) R.string.common_ask_again else R.string.common_to_settings
        ) {
            if (level == CAN_ASK_AGAIN) {
                setTitle(R.string.common_permission_permissions_needed_title)
                setMessage(R.string.common_permission_permissions_needed_message)
            } else {
                setTitle(R.string.common_permission_permissions_denied_title)
                setMessage(R.string.common_permission_permissions_denied_message)
            }
        }
    }
}

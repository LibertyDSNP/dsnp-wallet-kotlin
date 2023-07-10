package com.unfinished.common.presentation.scan

import android.Manifest
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.unfinished.common.base.BaseViewModel
import com.unfinished.common.utils.Event
import com.unfinished.common.utils.permissions.PermissionsAsker
import com.unfinished.common.utils.sendEvent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

abstract class ScanQrViewModel(
    private val permissionsAsker: PermissionsAsker.Presentation,
) : BaseViewModel(), PermissionsAsker by permissionsAsker {

    val scanningAvailable = MutableStateFlow(false)

    private val _resetScanningEvent = MutableLiveData<Event<Unit>>()
    val resetScanningEvent: LiveData<Event<Unit>> = _resetScanningEvent

    protected abstract suspend fun scanned(result: String)

    fun onScanned(result: String) {
        launch {
            scanned(result)
        }
    }

    fun onStart() {
        requirePermissions()
    }

    protected fun resetScanning() {
        _resetScanningEvent.sendEvent()
    }

    private fun requirePermissions() = launch {
        val granted = permissionsAsker.requirePermissionsOrExit(Manifest.permission.CAMERA)

        scanningAvailable.value = granted
    }
}

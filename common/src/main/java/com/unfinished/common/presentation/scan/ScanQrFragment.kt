package com.unfinished.common.presentation.scan

import android.view.WindowManager
import androidx.annotation.CallSuper
import com.unfinished.common.base.BaseFragment
import com.unfinished.common.utils.permissions.setupPermissionAsker

abstract class ScanQrFragment<V : ScanQrViewModel> : BaseFragment<V>() {

    abstract val scanView: ScanView

    @CallSuper
    override fun initViews() {
        startScanning()
    }

    @CallSuper
    override fun subscribe(viewModel: V) {
        viewModel.scanningAvailable.observe { scanningAvailable ->
            if (scanningAvailable) {
                scanView.resume()
            } else {
                scanView.pause()
            }
        }

        viewModel.resetScanningEvent.observeEvent {
            startScanning()
        }

        setupPermissionAsker(viewModel)
    }

    override fun onStart() {
        super.onStart()

        viewModel.onStart()
    }

    override fun onResume() {
        super.onResume()

        if (viewModel.scanningAvailable.value) {
            scanView.resume()
        }

        requireActivity().window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }

    override fun onPause() {
        super.onPause()

        scanView.pause()

        requireActivity().window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }

    private fun startScanning() {
        scanView.startDecoding { viewModel.onScanned(it) }
    }
}

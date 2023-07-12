package com.unfinished.dsnp_wallet_kotlin.ui.test

import androidx.compose.runtime.Composable
import androidx.compose.ui.viewinterop.AndroidViewBinding
import com.ramcosta.composedestinations.annotation.Destination
import com.unfinished.dsnp_wallet_kotlin.databinding.TestScreenBinding
import com.unfinished.dsnp_wallet_kotlin.ui.TestNavGraph
import com.unfinished.account.presentation.test.TestFragment

@TestNavGraph(start = true)
@Destination
@Composable
fun TestScreen() {
    AndroidViewBinding(TestScreenBinding::inflate) {
        val myFragment = fragmentContainerView.getFragment<TestFragment>()
    }
}
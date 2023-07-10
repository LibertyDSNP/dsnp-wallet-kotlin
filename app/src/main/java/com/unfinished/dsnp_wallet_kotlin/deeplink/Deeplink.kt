package com.unfinished.dsnp_wallet_kotlin.deeplink

import com.unfinished.dsnp_wallet_kotlin.BuildConfig

sealed class Deeplink {

    /**
     * TODO: https://github.com/raamcosta/compose-destinations/issues/178
     * We are hardcoding these values until the above issue has been resolved
     */
    companion object {
        const val JUMP_TO_APP: String = "https://dev-custodial-wallet.liberti.social/jumpIntoTheApp"
    }

    data class Valid(
        val url: String,
        private val onLinkUsed: () -> Unit
    ) : Deeplink() {
        val path: String
            get() {
                onLinkUsed()
                return url.split(BuildConfig.WEB_URL)[1]
            }
    }

    object None : Deeplink()
}


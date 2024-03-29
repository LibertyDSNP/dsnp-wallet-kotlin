package com.unfinished.account.presentation.model.chain

import android.widget.ImageView
import coil.ImageLoader
import coil.load
import com.unfinished.common.R

data class ChainUi(
    val id: String,
    val name: String,
    val icon: String?
)

fun ImageView.loadChainIcon(icon: String?, imageLoader: ImageLoader) {
    load(icon, imageLoader) {
        placeholder(R.drawable.bg_chain_placeholder)
        error(R.drawable.bg_chain_placeholder)
    }
}

fun ImageView.loadTokenIcon(icon: String?, imageLoader: ImageLoader) {
    load(icon, imageLoader) {
        fallback(R.drawable.ic_nova)
    }
}

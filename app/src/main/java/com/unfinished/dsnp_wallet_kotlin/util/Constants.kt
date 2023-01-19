package com.unfinished.dsnp_wallet_kotlin.util

object Constants {
    const val BASE_URL = "http://dummy.com/"
    const val API_TIMEOUT_SEC: Long = 30
}

enum class MnemonicWordEvent{
    ADD,
    REMOVE
}
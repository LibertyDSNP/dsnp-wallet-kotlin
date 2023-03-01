package com.unfinished.feature_account.domain.model

class Currency(
    val code: String,
    val name: String,
    val symbol: String?,
    val category: Category,
    val popular: Boolean,
    val id: Int,
    val coingeckoId: String,
    val selected: Boolean,
) {

    enum class Category {
        FIAT, CRYPTO
    }
}

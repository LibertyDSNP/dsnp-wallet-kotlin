package com.unfinished.common.address

import jp.co.soramitsu.fearless_utils.runtime.AccountId

class AccountIdKey(val value: AccountId) {

    override fun equals(other: Any?): Boolean {
        return this === other || other is com.unfinished.common.address.AccountIdKey && this.value contentEquals other.value
    }

    override fun hashCode(): Int = value.contentHashCode()

    override fun toString(): String = value.contentToString()
}

fun AccountId.intoKey() = com.unfinished.common.address.AccountIdKey(this)

operator fun <T> Map<com.unfinished.common.address.AccountIdKey, T>.get(key: AccountId) = get(
    com.unfinished.common.address.AccountIdKey(key)
)
fun <T> Map<com.unfinished.common.address.AccountIdKey, T>.getValue(key: AccountId) = getValue(
    com.unfinished.common.address.AccountIdKey(key)
)

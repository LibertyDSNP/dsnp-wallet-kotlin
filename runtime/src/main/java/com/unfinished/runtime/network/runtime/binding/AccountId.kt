package com.unfinished.runtime.network.runtime.binding

import jp.co.soramitsu.fearless_utils.runtime.AccountId

@HelperBinding
fun bindAccountId(dynamicInstance: Any?) = dynamicInstance.cast<AccountId>()

package com.unfinished.account.presentation.mnemonic

import jp.co.soramitsu.fearless_utils.encrypt.mnemonic.Mnemonic

fun Mnemonic.spacedWords(spacing: Int = 2) = wordList.joinToString(separator = " ".repeat(spacing))


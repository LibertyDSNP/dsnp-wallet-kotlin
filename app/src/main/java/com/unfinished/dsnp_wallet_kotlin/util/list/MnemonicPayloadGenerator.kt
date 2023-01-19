package com.unfinished.dsnp_wallet_kotlin.util.list

import androidx.recyclerview.widget.DiffUtil
import com.unfinished.dsnp_wallet_kotlin.data.model.MnemonicWord

object MnemonicPayloadGenerator : PayloadGenerator<MnemonicWord>(MnemonicWord::removed, MnemonicWord::indexDisplay)

object DiffCallback : DiffUtil.ItemCallback<MnemonicWord>() {

    override fun areItemsTheSame(oldItem: MnemonicWord, newItem: MnemonicWord): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: MnemonicWord, newItem: MnemonicWord): Boolean {
        return oldItem == newItem
    }

    override fun getChangePayload(oldItem: MnemonicWord, newItem: MnemonicWord): Any? {
        return MnemonicPayloadGenerator.diff(oldItem, newItem)
    }
}
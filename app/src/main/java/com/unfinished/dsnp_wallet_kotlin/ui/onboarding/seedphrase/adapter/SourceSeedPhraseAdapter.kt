package com.unfinished.dsnp_wallet_kotlin.ui.onboarding.seedphrase.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.unfinished.dsnp_wallet_kotlin.R
import com.unfinished.dsnp_wallet_kotlin.data.model.MnemonicWord
import com.unfinished.dsnp_wallet_kotlin.databinding.SourceSeedPhraseItemLayoutBinding
import com.unfinished.dsnp_wallet_kotlin.util.list.DiffCallback
import com.unfinished.dsnp_wallet_kotlin.util.list.resolvePayload
import com.unfinished.dsnp_wallet_kotlin.util.setTextOrHide
import com.unfinished.dsnp_wallet_kotlin.util.setVisible

class SourceSeedPhraseAdapter(
    val onItemClicked: ((Int, MnemonicWord) -> Unit)? = null,
) : ListAdapter<MnemonicWord, SourceSeedPhraseAdapter.MyViewHolder>(DiffCallback) {

    private var list: List<MnemonicWord> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = SourceSeedPhraseItemLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return MyViewHolder(itemView)
    }

    fun updateList(list: List<MnemonicWord>){
        this.list = list
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: SourceSeedPhraseAdapter.MyViewHolder, position: Int) {
        val item = list[position]
        holder.bind(item)
    }

    override fun onBindViewHolder(holder: SourceSeedPhraseAdapter.MyViewHolder, position: Int, payloads: MutableList<Any>) {
        val item = list[position]
        resolvePayload(holder, position, payloads) {
            when (it) {
                MnemonicWord::removed -> holder.bindState(item)
                MnemonicWord::indexDisplay -> {}
            }
        }
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class MyViewHolder(private val binding: SourceSeedPhraseItemLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: MnemonicWord) = with(binding) {
            this.item.text = "${item.content}"
            bindState(item)
        }

        fun bindState(item: MnemonicWord) = with(binding) {
            val hasWord = !item.removed

            root.isEnabled = hasWord

            if (item.removed) {
                binding.item.background = ContextCompat.getDrawable(
                    binding.root.context,
                    R.drawable.seed_phrase_empty_dot_bg
                )
                root.setOnClickListener(null)
            } else {
                binding.item.background = ContextCompat.getDrawable(
                    binding.root.context,
                    R.drawable.seed_phrase_fill_bg
                )
                root.setOnClickListener {  onItemClicked?.invoke(adapterPosition,item) }
            }
        }
    }
}
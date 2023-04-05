package com.unfinished.feature_account.presentation.mnemonic.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.unfinished.feature_account.R
import com.unfinished.feature_account.databinding.ItemSourceWordBinding
import com.unfinished.feature_account.presentation.mnemonic.confirm.MnemonicWord
import com.unfinished.feature_account.presentation.mnemonic.util.list.DiffCallback
import io.novafoundation.nova.common.list.resolvePayload
import io.novafoundation.nova.common.utils.setOnSafeClickListener

class SourceWordAdapter(
    val onItemClicked: ((Int, MnemonicWord) -> Unit)? = null,
) : ListAdapter<MnemonicWord, SourceWordAdapter.MyViewHolder>(DiffCallback) {

    private var list: List<MnemonicWord> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = ItemSourceWordBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return MyViewHolder(itemView)
    }

    fun updateList(list: List<MnemonicWord>){
        this.list = list
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = list[position]
        holder.bind(item)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int, payloads: MutableList<Any>) {
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

    inner class MyViewHolder(private val binding: ItemSourceWordBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: MnemonicWord) = with(binding) {
            this.item.text = item.content
            bindState(item)
        }

        fun bindState(item: MnemonicWord) = with(binding) {
            val hasWord = !item.removed

            root.isEnabled = hasWord

            if (item.removed) {
                binding.item.text = ""
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
                root.setOnSafeClickListener {  onItemClicked?.invoke(adapterPosition,item) }
            }
        }
    }
}
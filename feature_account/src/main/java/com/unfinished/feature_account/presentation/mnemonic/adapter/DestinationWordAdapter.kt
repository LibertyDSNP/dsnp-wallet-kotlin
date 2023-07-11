package com.unfinished.feature_account.presentation.mnemonic.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.unfinished.feature_account.R
import com.unfinished.feature_account.databinding.ItemDestinationWordBinding
import com.unfinished.feature_account.presentation.mnemonic.confirm.MnemonicWord
import com.unfinished.feature_account.presentation.mnemonic.util.list.DiffCallback
import com.unfinished.common.utils.setOnSafeClickListener
import com.unfinished.feature_account.presentation.mnemonic.util.list.resolvePayload

class DestinationWordAdapter(
    val onItemClicked: ((Int, MnemonicWord) -> Unit)? = null,
) : ListAdapter<MnemonicWord, DestinationWordAdapter.MyViewHolder>(DiffCallback) {

    private var list: List<MnemonicWord> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = ItemDestinationWordBinding.inflate(LayoutInflater.from(parent.context),parent,false)
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

    inner class MyViewHolder(private val binding: ItemDestinationWordBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: MnemonicWord) = with(binding) {
            bindState(item)
        }

        fun bindState(item: MnemonicWord) = with(binding) {
            val hasWord = !item.removed

            root.isEnabled = hasWord

            if (item.removed) {
                this.item.text = "${item.indexDisplay} ${item.content}"
                binding.item.background = ContextCompat.getDrawable(
                    binding.root.context,
                    R.drawable.seed_phrase_empty_bg
                )
                root.setOnClickListener(null)
            } else {
                this.item.text = "${item.indexDisplay} ${item.content}"
                binding.item.background = ContextCompat.getDrawable(
                    binding.root.context,
                    R.drawable.seed_phrase_fill_bg
                )
                root.setOnSafeClickListener {  onItemClicked?.invoke(adapterPosition,item) }
            }
        }
    }
}
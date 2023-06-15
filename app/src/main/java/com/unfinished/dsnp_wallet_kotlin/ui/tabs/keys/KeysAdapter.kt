package com.unfinished.dsnp_wallet_kotlin.ui.tabs.keys

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.unfinished.dsnp_wallet_kotlin.R
import com.unfinished.dsnp_wallet_kotlin.data.model.Key
import com.unfinished.dsnp_wallet_kotlin.databinding.KeysItemLayoutBinding
import com.unfinished.dsnp_wallet_kotlin.util.setBackgroundTint

class KeysAdapter(
    val onItemClicked: ((Int, Key) -> Unit)? = null,
) : RecyclerView.Adapter<KeysAdapter.MyViewHolder>() {

    private var list: MutableList<Key> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = KeysItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = list[position]
        holder.bind(item, position)
    }

    fun updateList(list: MutableList<Key>) {
        this.list = list
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class MyViewHolder(private val binding: KeysItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Key, position: Int) {
            binding.rootKeyLabel.text = item.label
            binding.rootKey.text = item.key
            binding.keyChainRl.background = ContextCompat.getDrawable(
                binding.keyChainRl.context,
                if (item.isConnected) R.drawable.round_3dp_rect_button_color else R.drawable.round_3dp_rect_white
            )
        }
    }
}
package com.unfinished.feature_account.presentation.mnemonic.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.unfinished.feature_account.databinding.ItemBackupMnemonicWordBinding
import com.unfinished.feature_account.presentation.mnemonic.confirm.MnemonicWord

class BackupWordAdapter(
    private var list: List<MnemonicWord>
) : RecyclerView.Adapter<BackupWordAdapter.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = ItemBackupMnemonicWordBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = list[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class MyViewHolder(private val binding: ItemBackupMnemonicWordBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: MnemonicWord) {
            binding.item.setText("${item.indexDisplay} ${item.content}")
        }
    }
}
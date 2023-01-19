package com.unfinished.dsnp_wallet_kotlin.ui.onboarding.seedphrase

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.unfinished.dsnp_wallet_kotlin.databinding.SeedPhraseItemLayoutBinding

class SeedPhraseAdapter(
    private var list: List<String>
) : RecyclerView.Adapter<SeedPhraseAdapter.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = SeedPhraseItemLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = list[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class MyViewHolder(private val binding: SeedPhraseItemLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: String) {
            binding.item.setText(item)
        }
    }
}
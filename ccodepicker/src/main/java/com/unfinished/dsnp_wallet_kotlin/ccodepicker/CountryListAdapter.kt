package com.unfinished.dsnp_wallet_kotlin.ccodepicker

import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.unfinished.dsnp_wallet_kotlin.ccodepicker.databinding.CountryItemBinding


class CountryListAdapter(
    val onItemClicked: ((Int, Country) -> Unit)? = null,
) : RecyclerView.Adapter<CountryListAdapter.MyViewHolder>() {

    private var list: List<Country> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = CountryItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = list[position]
        holder.bind(item)
    }

    fun updateList(list: List<Country>) {
        this.list = list
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class MyViewHolder(private val binding: CountryItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Country) {
            binding.flag.text = localeToEmoji(item.code)
            binding.name.text = item.name + " (${item.code})"
            binding.code.text = item.dial_code
            binding.rootLayout.setOnClickListener {
                onItemClicked?.invoke(adapterPosition,item)
            }
        }
    }
}
package com.gulshan.moneyfront.ui.mf_list.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.gulshan.moneyfront.data.Mf
import com.gulshan.moneyfront.databinding.MfItemBinding

class MfAdapter(private val listener: OnItemClickListener) :
    PagingDataAdapter<Mf, MfAdapter.MfViewHolder>(MF_COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MfViewHolder {
        val binding =
            MfItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MfViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MfViewHolder, position: Int) {
        val currItem = getItem(position)
        if (currItem != null) {
            holder.bind(currItem)
        }
    }

    inner class MfViewHolder(private val binding: MfItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.buttonInvest.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val item = getItem(position)
                    if (item != null) {
                        listener.onItemClick(item)
                    }
                }
            }
        }

        fun bind(currMf: Mf) {
            binding.apply {
                if (currMf.Mutual_Fund_Family != null)
                    textFamily.text = currMf.Mutual_Fund_Family
                if (currMf.Scheme_Name != null)
                    textName.text = currMf.Scheme_Name
                if (currMf.min_investment != null)
                    textMinValue.text = currMf.min_investment
                if (currMf.NAV != null)
                    textNavValue.text = currMf.NAV
                if (currMf.returns != null)
                    textReturnValue.text = currMf.returns
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(mf: Mf)
    }

    companion object {

        public val MF_COMPARATOR = object : DiffUtil.ItemCallback<Mf>() {
            override fun areContentsTheSame(oldItem: Mf, newItem: Mf): Boolean {
                return oldItem.Scheme_Code == newItem.Scheme_Code
            }

            override fun areItemsTheSame(oldItem: Mf, newItem: Mf): Boolean {
                return oldItem == newItem
            }
        }
    }
}
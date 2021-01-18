package hu.kole.test.kotlin.adapter

import android.databinding.DataBindingUtil
import android.support.v7.recyclerview.extensions.ListAdapter
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import hu.kole.test.R
import hu.kole.test.data.Item
import hu.kole.test.databinding.ViewItemBinding

class ItemAdapter: ListAdapter<Item, ItemViewHolder>(ItemDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.view_item, parent, false))
    }

    override fun onBindViewHolder(viewHolder: ItemViewHolder, position: Int) {
        getItem(position)?.let { item ->
            viewHolder.bind(item)
        }
    }
}

class ItemViewHolder(private val binding: ViewItemBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(item: Item) {
        binding.item = item
        binding.executePendingBindings()
    }
}

class ItemDiffCallback : DiffUtil.ItemCallback<Item>() {
    override fun areItemsTheSame(oldItem: Item, newItem: Item): Boolean {
        return oldItem.title == newItem.title
    }

    override fun areContentsTheSame(oldItem: Item, newItem: Item): Boolean {
        return oldItem.title == newItem.title
    }
}

